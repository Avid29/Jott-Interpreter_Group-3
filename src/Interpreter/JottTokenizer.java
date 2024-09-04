package src.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Adam Dernis
 **/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import src.Interpreter.Tokenization.TokenizerError;
import src.Interpreter.Tokenization.TokenizerState;
import src.Interpreter.Tokenization.TokenizerStateHandler;

public class JottTokenizer {
  private static final Map<TokenizerState, Map<Character, TokenizerStateHandler>> handlerMaps = mapsInitializer();

  private ArrayList<Token> tokens;
  private String tokenProgress;
  private TokenizerState state;
  private TokenizerStateHandler handler;
  private Map<Character, TokenizerStateHandler> handlerMap;

  private String filename;
  private int lineNum;
  private int columnNum;

  public JottTokenizer(String filename) {
    this.filename = filename;
    lineNum = 1;
    columnNum = 1;

    updateState(TokenizerState.START);
    tokenProgress = "";
    tokens = new ArrayList<>();
  }

  /**
   * Update the tokenizer state a single character at a time
   */
  public TokenizerError handleCharacter(char c) {

    // Track the line and column number
    if (c == '\n') {
      lineNum++;
      columnNum = 1;
    } else {
      columnNum++;
    }

    // Discard whitespace between tokens
    if (state == TokenizerState.START && Character.isWhitespace(c)) {
      return null;
    }

    if (handlerMap.containsKey(c)) {
      // The current character is part of the in-progress token,
      // or is a complete token on its own.

      // Add the character to the token.
      tokenProgress += c;

      // Grab the new handler
      handler = handlerMap.get(c);
      if (handler.tokenEnd()) {
        // This character completes the token.
        processToken();
      }

      // Update the state
      updateState(handler.getNewState());
    } else if (state == TokenizerState.COMMENT) {
      // We'll lightly track comments as tokens for debugging purposes
      // This fake token will get thrown away when it ends
      tokenProgress += c;
      return null;
    } else if (handler != null && handler.getCreatedTokenType() == null) {
      // Oh no! There's been a syntax error
      // The in-progress token was not completed by the current character, and was not
      // in a valid state to end.
      TokenizerError error = new TokenizerError(filename, lineNum, columnNum, state, tokenProgress, c);
      updateState(TokenizerState.FAULTED);
      return error;
    } else {
      // This character does not belong to the in-progress token,
      // However, the token was in a valid state to end
      processToken();

      // Return to the START state and handle this character as the beginning of a new
      // token
      updateState(TokenizerState.START);
      return handleCharacter(c);
    }

    return null;
  }

  /**
   * Completes the in-progress token and marks the file as tokenized
   */
  public TokenizerError finalizeTokens() {
    if (handler.getCreatedTokenType() == null) {
      TokenizerError error = new TokenizerError(filename, lineNum, columnNum, state, tokenProgress, '\0');
      updateState(TokenizerState.FAULTED);
      return error;
    }

    if (!handler.tokenEnd()) {
      processToken();
    }

    updateState(TokenizerState.DONE);
    return null;
  }

  /**
   * Updates the tokenizer's state machine, including the handlerMap
   */
  private void updateState(TokenizerState state) {
    this.state = state;

    if (state == TokenizerState.FAULTED) {
      handlerMap = null;
      return;
    }

    handlerMap = handlerMaps.get(state);
  }

  /**
   * Completes the current token (if applicaple) and resets the token progress
   */
  private void processToken() {
    if (handler == null || handler.getCreatedTokenType() == null) {
      tokenProgress = "";
      return;
    }

    tokens.add(new Token(tokenProgress, filename, lineNum, handler.getCreatedTokenType()));
    tokenProgress = "";
  }

  public ArrayList<Token> getTokens() {
    return tokens;
  }

  /**
   * Takes in a filename and tokenizes that file into Tokens
   * based on the rules of the Jott Language
   * 
   * @param filename the name of the file to tokenize; can be relative or absolute
   *                 path
   * @return an ArrayList of Jott Tokens
   */
  public static ArrayList<Token> tokenize(String filename) {

    // Initialize an instance of the tokenizer to track our state machine.
    JottTokenizer tokenizer = new JottTokenizer(filename);

    // We're only collecting a single error at a time.
    // When this is null, we haven't found any errors yet.
    TokenizerError error = null;

    // Open the file for reading
    File file = new File("test/" + filename);
    try (InputStream in = new FileInputStream(file);
        Reader reader = new InputStreamReader(in, Charset.defaultCharset());
        Reader buffer = new BufferedReader(reader)) {

      // Read the file one character at a time into the tokenizer. 
      int r;
      while ((r = reader.read()) != -1) {
        char c = (char) r;
        error = tokenizer.handleCharacter(c);
        
        // If we hit an error, log it and stop iterating the file.
        if (error != null) {
          break;
        }
      }
      
    } catch (IOException exec) {
      // The file could not be found or opened.
      System.err.printf("There was an issue reading file \"%s\".", filename);
      return null;
    }

    // Finalize the tokenization if not errors were found so far.
    if (error == null) {
      error = tokenizer.finalizeTokens();
    }

    if (error != null) {
      // There was a syntax error while tokenizing.
      // Output the error message and details.
      System.err.println(error.toString());

      // Return null since the tokens were not reliably identified.
      return null;
    }

    // The file was successfully tokenized.
    // Return the results.
    return tokenizer.getTokens();
  }

  private static void putDigits(HashMap<Character, TokenizerStateHandler> map, TokenizerStateHandler handler) {
    for (char c = '0'; c <= '9'; c++) {
      map.put(c, handler);
    }
  }

  /**
   * Add a handler to the map for all letter characters
   */
  private static void putLetters(HashMap<Character, TokenizerStateHandler> map, TokenizerStateHandler handler) {
    for (char c = 'A'; c <= 'Z'; c++) {
      map.put(c, handler);
      map.put((char) (c + 'a' - 'A'), handler);
    }
  }

  /**
   * Add a handler to the map for all digit characters
   */
  private static Map<Character, TokenizerStateHandler> buildStartMap() {
    HashMap<Character, TokenizerStateHandler> startMap = new HashMap<>();

    // These are all one character tokens
    startMap.put(',', new TokenizerStateHandler(TokenizerState.START, TokenType.COMMA));
    startMap.put(']', new TokenizerStateHandler(TokenizerState.START, TokenType.R_BRACKET));
    startMap.put('[', new TokenizerStateHandler(TokenizerState.START, TokenType.L_BRACKET));
    startMap.put('}', new TokenizerStateHandler(TokenizerState.START, TokenType.R_BRACE));
    startMap.put('{', new TokenizerStateHandler(TokenizerState.START, TokenType.L_BRACE));
    startMap.put(';', new TokenizerStateHandler(TokenizerState.START, TokenType.SEMICOLON));

    // These could be one character tokens, or they could be the start of a two
    // character token
    startMap.put('=', new TokenizerStateHandler(TokenizerState.ASSIGN, TokenType.ASSIGN));
    startMap.put(':', new TokenizerStateHandler(TokenizerState.COLON, TokenType.COLON));

    // These characters are the start of incomplete tokens (or a comment)
    startMap.put('.', new TokenizerStateHandler(TokenizerState.DECIMAL, null));
    startMap.put('!', new TokenizerStateHandler(TokenizerState.BANG, null));
    startMap.put('"', new TokenizerStateHandler(TokenizerState.STRING, null));
    startMap.put('#', new TokenizerStateHandler(TokenizerState.COMMENT, null));

    // Relation operator character handler from the start state
    TokenizerStateHandler relOpHandlerStart = new TokenizerStateHandler(TokenizerState.REL_OP, TokenType.REL_OP);
    startMap.put('<', relOpHandlerStart);
    startMap.put('>', relOpHandlerStart);

    // Math operator character handler from the start state
    TokenizerStateHandler mathOpHandlerStart = new TokenizerStateHandler(TokenizerState.START, TokenType.MATH_OP);
    startMap.put('+', mathOpHandlerStart);
    startMap.put('-', mathOpHandlerStart);
    startMap.put('*', mathOpHandlerStart);
    startMap.put('/', mathOpHandlerStart);

    // Digit character handler from the start state
    TokenizerStateHandler digitHandlerStart = new TokenizerStateHandler(TokenizerState.INTERGER, TokenType.NUMBER);
    putDigits(startMap, digitHandlerStart);

    // Letter character handler from the start state
    TokenizerStateHandler letterHandlerStart = new TokenizerStateHandler(TokenizerState.ID, TokenType.ID_KEYWORD);
    putLetters(startMap, letterHandlerStart);
    return startMap;
  }

  /**
   * Builds all handler maps and a map to them from the tokenizer state
   * 
   * @return A map of handler maps.
   */
  private static Map<TokenizerState, Map<Character, TokenizerStateHandler>> mapsInitializer() {
    // The handler map for the start state is especially large, so it's built in its
    // own function
    Map<Character, TokenizerStateHandler> startMap = buildStartMap();

    // Though they are each different states, ASSIGN, REL_OP and BANG contain the
    // same handler for '='
    // This is the only handler for those 3 states
    TokenizerStateHandler relOpCompletionHandler = new TokenizerStateHandler(TokenizerState.START, TokenType.REL_OP);
    Map<Character, TokenizerStateHandler> assignMap = new HashMap<>();
    Map<Character, TokenizerStateHandler> relOpMap = new HashMap<>();
    Map<Character, TokenizerStateHandler> bangMap = new HashMap<>();
    assignMap.put('=', relOpCompletionHandler);
    relOpMap.put('=', relOpCompletionHandler);
    bangMap.put('=', relOpCompletionHandler);

    // In the decimal state only digits can be handled to make a float
    HashMap<Character, TokenizerStateHandler> decimalMap = new HashMap<>();
    putDigits(decimalMap, new TokenizerStateHandler(TokenizerState.FLOAT, TokenType.NUMBER));

    // In the integer state we will handle '.' to transition to floats, and digits
    // to remain and integer
    HashMap<Character, TokenizerStateHandler> integerMap = new HashMap<>();
    integerMap.put('.', new TokenizerStateHandler(TokenizerState.FLOAT, TokenType.NUMBER));
    putDigits(integerMap, new TokenizerStateHandler(TokenizerState.INTERGER, TokenType.NUMBER));

    // In the float state only digits can be handled to continue the float
    HashMap<Character, TokenizerStateHandler> floatMap = new HashMap<>();
    putDigits(floatMap, new TokenizerStateHandler(TokenizerState.FLOAT, TokenType.NUMBER));

    // Once we're in the id state, either digits or letters can be appended to
    // continue the id
    HashMap<Character, TokenizerStateHandler> idMap = new HashMap<>();
    TokenizerStateHandler idContHandlerId = new TokenizerStateHandler(TokenizerState.ID, TokenType.ID_KEYWORD);
    putDigits(idMap, idContHandlerId);
    putLetters(idMap, idContHandlerId);

    // After a colon, we can handle another colon to become a function header
    Map<Character, TokenizerStateHandler> colonMap = new HashMap<>();
    colonMap.put(':', new TokenizerStateHandler(TokenizerState.START, TokenType.FC_HEADER));

    // In a string digits, letters, and spaces can be appended to the string. A '"'
    // ends the string.
    HashMap<Character, TokenizerStateHandler> stringMap = new HashMap<>();
    TokenizerStateHandler stringContHandlerString = new TokenizerStateHandler(TokenizerState.STRING, null);
    putDigits(stringMap, stringContHandlerString);
    putLetters(stringMap, stringContHandlerString);
    stringMap.put(' ', stringContHandlerString);
    stringMap.put('"', new TokenizerStateHandler(TokenizerState.START, TokenType.STRING));

    // Comments end once a newline is detected. In the meantime, they eat all other
    // characters.
    Map<Character, TokenizerStateHandler> commentMap = new HashMap<>();
    commentMap.put('\n', new TokenizerStateHandler(TokenizerState.START, null));

    // Java really isn't my language, but I believe this is the best way to
    // construct an immutable map.
    // In a better language this would be constructed in compile time and simply
    // loaded to memory.
    return Map.ofEntries(
        Map.entry(TokenizerState.START, Collections.unmodifiableMap(startMap)),
        Map.entry(TokenizerState.ASSIGN, Collections.unmodifiableMap(assignMap)),
        Map.entry(TokenizerState.REL_OP, Collections.unmodifiableMap(relOpMap)),
        Map.entry(TokenizerState.BANG, Collections.unmodifiableMap(bangMap)),
        Map.entry(TokenizerState.DECIMAL, Collections.unmodifiableMap(decimalMap)),
        Map.entry(TokenizerState.INTERGER, Collections.unmodifiableMap(integerMap)),
        Map.entry(TokenizerState.FLOAT, Collections.unmodifiableMap(floatMap)),
        Map.entry(TokenizerState.ID, Collections.unmodifiableMap(idMap)),
        Map.entry(TokenizerState.COLON, Collections.unmodifiableMap(colonMap)),
        Map.entry(TokenizerState.STRING, Collections.unmodifiableMap(stringMap)),
        Map.entry(TokenizerState.COMMENT, Collections.unmodifiableMap(commentMap)));
  }
}
