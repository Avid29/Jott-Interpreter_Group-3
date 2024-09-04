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
 * @author 
 **/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import src.Interpreter.Tokenization.TokenizerError;
import src.Interpreter.Tokenization.TokenizerState;
import src.Interpreter.Tokenization.TokenizerStateHandler;

public class JottTokenizer
{
  private static final Map<TokenizerState, Map<Character, TokenizerStateHandler>> handlerMaps = mapsInitializer();

  private ArrayList<Token> tokens;
  private String tokenProgress;
  private TokenizerState state;
  private TokenizerStateHandler handler; 
  private Map<Character, TokenizerStateHandler> handlerMap;
  
  private String filename;
  private int lineNum;

  public JottTokenizer(String filename)
  {
    this.filename = filename;
    lineNum = 1;

    updateState(TokenizerState.START);
    tokenProgress = "";
    tokens = new ArrayList<>();
  }

  public TokenizerError handleCharacter(char c)
  {
    if (c == '\n')
    {
      lineNum++;
    }
    
    // Discard whitespace between tokens
    if (state == TokenizerState.START && Character.isWhitespace(c))
    {
      return null;
    }

    if(handlerMap.containsKey(c))
    {
      tokenProgress += c;

      handler = handlerMap.get(c);
      if (handler.tokenEnd())
      {
        processToken();
      }

      updateState(handler.getNewState());
    }
    else if (state == TokenizerState.COMMENT)
    {
      // We'll lightly track comments as tokens for debugging purposes
      tokenProgress += c;
      return null;
    }
    else if (handler != null && handler.getCreatedTokenType() == null)
    {
      updateState(TokenizerState.FAULTED);

      // TODO: Gather error details
      
      return new TokenizerError();
    }
    else
    {
      // This character does not belong to the in-progress token.
      processToken();
      updateState(TokenizerState.START);
      handleCharacter(c);
    }

    return null;
  }

  public TokenizerError finalizeTokens()
  {
    if (handler.getCreatedTokenType() == null)
    {
      updateState(TokenizerState.FAULTED);

      // TODO: Log error

      return new TokenizerError();
    }
    
    if (!handler.tokenEnd()){
      processToken();
    }

    updateState(TokenizerState.DONE);
    return null;
  }

  private void updateState(TokenizerState state)
  {
    this.state = state;

    if (state == TokenizerState.FAULTED)
    {
      handlerMap = null;
      return;
    }

    handlerMap = handlerMaps.get(state);
  }

  private void processToken()
  {
    if (handler == null || handler.getCreatedTokenType() == null)
    {
      tokenProgress = "";
      return;
    }

    tokens.add(new Token(tokenProgress, filename, lineNum, handler.getCreatedTokenType()));
    tokenProgress = "";
  }

  public ArrayList<Token> getTokens()
  {
    return tokens;
  }

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename)
    {
      JottTokenizer tokenizer = new JottTokenizer(filename); 

      File file = new File("test/" + filename);
      try (InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in, Charset.defaultCharset());
            Reader buffer = new BufferedReader(reader))
            {
              int r;
              while ((r = reader.read()) != -1)
              {
                  char c = (char)r;
                  TokenizerError error = tokenizer.handleCharacter(c);
                  if (error != null)
                  {
                    // TODO: Handle error
                    return null;
                  }
              }
            }
            catch (IOException exec)
            {
              // TODO: Log missing file error
              return null;
            }
      
      TokenizerError error = tokenizer.finalizeTokens();
      if (error != null) {
        // TODO: Handle error
        return null;  
      }

		  return tokenizer.getTokens();
    }

    private static Map<TokenizerState, Map<Character, TokenizerStateHandler>> mapsInitializer()
    {
      Map<Character, TokenizerStateHandler> startMap = new HashMap<>();
      startMap.put(',', new TokenizerStateHandler(TokenizerState.START, TokenType.COMMA, true));
      startMap.put(']', new TokenizerStateHandler(TokenizerState.START, TokenType.R_BRACKET, true));
      startMap.put('[', new TokenizerStateHandler(TokenizerState.START, TokenType.L_BRACKET, true));
      startMap.put('}', new TokenizerStateHandler(TokenizerState.START, TokenType.R_BRACE, true));
      startMap.put('{', new TokenizerStateHandler(TokenizerState.START, TokenType.L_BRACE, true));
      startMap.put('=', new TokenizerStateHandler(TokenizerState.ASSIGN, TokenType.ASSIGN, false));
      startMap.put(';', new TokenizerStateHandler(TokenizerState.START, TokenType.SEMICOLON, true));
      startMap.put('.', new TokenizerStateHandler(TokenizerState.DECIMAL, null, false));
      startMap.put(':', new TokenizerStateHandler(TokenizerState.COLON, TokenType.COLON, false));
      startMap.put('!', new TokenizerStateHandler(TokenizerState.BANG, null, false));
      startMap.put('"', new TokenizerStateHandler(TokenizerState.STRING, null, false));
      startMap.put('#', new TokenizerStateHandler(TokenizerState.COMMENT, null, false));

      // Relation operator character handler from the start state
      TokenizerStateHandler relOpHandlerStart = new TokenizerStateHandler(TokenizerState.REL_OP, TokenType.REL_OP, false);
      startMap.put('<', relOpHandlerStart);
      startMap.put('>', relOpHandlerStart);
      
      // Math operator character handler from the start state
      TokenizerStateHandler mathOpHandlerStart = new TokenizerStateHandler(TokenizerState.START, TokenType.MATH_OP, true);
      startMap.put('+', mathOpHandlerStart);
      startMap.put('-', mathOpHandlerStart);
      startMap.put('*', mathOpHandlerStart);
      startMap.put('/', mathOpHandlerStart);

      // Digit character handler from the start state
      TokenizerStateHandler digitHandlerStart = new TokenizerStateHandler(TokenizerState.INTERGER, TokenType.NUMBER, false);
      for (char c = '0'; c <= '9'; c++)
      {
        startMap.put(c, digitHandlerStart);
      }

      // Letter character handler from the start state
      TokenizerStateHandler letterHandlerStart = new TokenizerStateHandler(TokenizerState.ID, TokenType.ID_KEYWORD, false);
      for (char c = 'A'; c <= 'Z'; c++)
      {
        startMap.put(c, letterHandlerStart);
        startMap.put((char)(c + 'a'-'A'), letterHandlerStart);
      }

      // Though they are each different states, ASSIGN, REL_OP and BANG contain the same handler for '='. 
      TokenizerStateHandler relOpCompletionHandler = new TokenizerStateHandler(TokenizerState.START, TokenType.REL_OP, true);
      Map<Character, TokenizerStateHandler> assignMap = new HashMap<>();
      Map<Character, TokenizerStateHandler> relOpMap = new HashMap<>();
      Map<Character, TokenizerStateHandler> bangMap  = new HashMap<>();
      assignMap.put('=', relOpCompletionHandler);
      relOpMap.put('=', relOpCompletionHandler);
      bangMap.put('=', relOpCompletionHandler);

      Map<Character, TokenizerStateHandler> decimalMap  = new HashMap<>();
      TokenizerStateHandler digitHandlerDecimal = new TokenizerStateHandler(TokenizerState.FLOAT, TokenType.NUMBER, false);
      for (char c = '0'; c <= '9'; c++)
      {
        decimalMap.put(c, digitHandlerDecimal);
      }

      Map<Character, TokenizerStateHandler> integerMap  = new HashMap<>();
      integerMap.put('.', new TokenizerStateHandler(TokenizerState.FLOAT, TokenType.NUMBER, false));
      TokenizerStateHandler digitHandlerInteger = new TokenizerStateHandler(TokenizerState.INTERGER, TokenType.NUMBER, false);
      for (char c = '0'; c <= '9'; c++)
      {
        integerMap.put(c, digitHandlerInteger);
      }

      Map<Character, TokenizerStateHandler> floatMap  = new HashMap<>();
      TokenizerStateHandler digitHandlerfloat = new TokenizerStateHandler(TokenizerState.FLOAT, TokenType.NUMBER, false);
      for (char c = '0'; c <= '9'; c++)
      {
        floatMap.put(c, digitHandlerfloat);
      }

      Map<Character, TokenizerStateHandler> idMap  = new HashMap<>();
      TokenizerStateHandler idContHandlerId = new TokenizerStateHandler(TokenizerState.ID, TokenType.ID_KEYWORD, false);
      for (char c = '0'; c <= '9'; c++)
      {
        idMap.put(c, idContHandlerId);
      }
      for (char c = 'A'; c <= 'Z'; c++)
      {
        idMap.put(c, idContHandlerId);
        idMap.put((char)(c + 'a'-'A'), idContHandlerId);
      }

      Map<Character, TokenizerStateHandler> colonMap  = new HashMap<>();
      colonMap.put(':', new TokenizerStateHandler(TokenizerState.START, TokenType.FC_HEADER, true));

      Map<Character, TokenizerStateHandler> stringMap  = new HashMap<>();
      TokenizerStateHandler stringContHandlerString = new TokenizerStateHandler(TokenizerState.STRING, null, false);
      stringMap.put(' ', stringContHandlerString);
      for (char c = '0'; c <= '9'; c++)
      {
        stringMap.put(c, stringContHandlerString);
      }
      for (char c = 'A'; c <= 'Z'; c++)
      {
        stringMap.put(c, stringContHandlerString);
        stringMap.put((char)(c + 'a'-'A'), stringContHandlerString);
      }
      stringMap.put('"', new TokenizerStateHandler(TokenizerState.START, TokenType.STRING, true));

      Map<Character, TokenizerStateHandler> commentMap  = new HashMap<>();
      commentMap.put('\n', new TokenizerStateHandler(TokenizerState.START, null, true));

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
