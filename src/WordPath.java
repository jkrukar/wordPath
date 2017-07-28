//***********************************
//John Krukar
//This class takes 2n+1 command line arguments
//The first command is a path to a dictionary.txt file
//The rest of the commands are pairs of words
//Output: the shortest path between word pairs
//Accepts words <= MAXWORDLENGTH
//***********************************

import java.util.ArrayList;
import java.util.PriorityQueue;

public class WordPath
{

  private final static int MAXWORDLENGTH = 15;
  private static ArrayList[] wordBank;

  //***********************************
  //Processes the provided dictionary:
  //Each new word is stored in a new WordNode
  //The adjacent words (words with a Levenshtein distance of 1 compared) are stored in the new WordNode edge list.
  //There are three edge lists containing words 1 char shorter, words of equal length, and words 1 char longer.
  //The new WordNode is stored in the wordBank according to the length of the node's word.
  //***********************************

  private void processDictionary(String dictionaryPath)
  {
    ReadTextFile fileReader = new ReadTextFile(dictionaryPath);
    String[] strArray = {""};

    while(strArray != null)
    {
      strArray = fileReader.readWordsOnLine();

      try
      {
        addToWordBank(strArray[0]);
      }
      catch(NullPointerException e)
      {
        //Catches nullPointer at the end of reading the dictionary
      }
    }

    fileReader.closeReader();
  }

  //***********************************
  //Initializes the wordBank array for storing WordNodes
  //The wordBank array has a length equal to MAXWORDLENGTH
  //Initializes ArrayLists for each wordBank index
  //The wordBank contains ArrayLists containing words with a length equal to the wordBank index + 1;
  //Ex: The ArrayList in wordBank[1] contains 2 letter words.
  //***********************************

  private void buildWordBank()
  {
    wordBank = new ArrayList[MAXWORDLENGTH];

    for(int i=0; i< MAXWORDLENGTH;i++)
    {
      ArrayList<WordNode> wordContainer = new ArrayList<>();
      wordBank[i] = wordContainer;
    }
  }

  //***********************************
  //Parameter: String newWord: a new word to be added to the word bank.
  //Creates a new WordNode to store newWord into.
  //Creates the edge list for the new WordNode.(edge list: list of words adjacent to newWord)
  //Adds the new WordNode into the word bank at index= newWord length - 1.
  //***********************************

  private void addToWordBank(String newWord)
  {
    int wordLength = newWord.length();
    WordNode newNode = new WordNode(newWord);
    wordBank[wordLength -1].add(newNode);
  }

  //***********************************
  //Finds the edges for every WordNode in 'wordBank' and stores them
  //Calls findShortEdges()
  //Calls findEqualEdges
  //No output
  //***********************************

  private void createEdgeLists()
  {
    ArrayList<WordNode> targetList;
    for(int i = 0; i < MAXWORDLENGTH; i++)
    {
      targetList = wordBank[i];

      for(WordNode nxtNode : targetList)
      {
        if(i > 0)
        {
          findShortEdges(nxtNode, i);
        }

        findEqualEdges(nxtNode, targetList, i);
      }
    }
  }

  //***********************************
  //WordNode templateNode - the node you want to find the short edges for
  //int listIndex - the index of wordBank that the templateNode is stored in
  //Finds words that are 1 char shorter than the template word and have a levenshtein distance of 1.
  //***********************************

  private void findShortEdges(WordNode templateNode, int listIndex)
  {

    ArrayList<WordNode> targetList = wordBank[listIndex - 1];
    String templateWord = templateNode.getWord();
    int tempIndex;
    int testIndex;
    int offSet;
    char templateLetter;
    char testLetter;
    String testWord;

    for(WordNode nxtNode : targetList)  //nxtNode words are shorter than template word
    {
      tempIndex = 0;
      testIndex = 0;
      offSet = 0;
      testWord = nxtNode.getWord();

      while(tempIndex != listIndex)
      {
        templateLetter = templateWord.charAt(tempIndex + offSet);
        testLetter = testWord.charAt(testIndex);

        if(templateLetter == testLetter)
        {
          tempIndex ++;
          testIndex ++;
        }
        else
        {
          offSet ++;

          if(offSet == 2)
          {
            break;
          }
        }
      }

      if(offSet < 2)  //Found a match
      {
        templateNode.edgeLists[0].add(nxtNode);
        nxtNode.edgeLists[2].add(templateNode);
      }
    }
  }

  //***********************************
  //WordNode nxtNode - the node you want to find equal length edges for
  //int listIndex - the index of wordBank that the templateNode is stored in
  //ArrayList targetList - the list the nxtNode is stored in
  //Finds words that are of equal length to nxtNode and have a levenshtein distance of 1.
  //***********************************

  private void findEqualEdges(WordNode nxtNode, ArrayList targetList, int listIndex)
  {
    String templateWord = nxtNode.getWord();
    String testWord;
    int testWordIndex;
    WordNode edgeNode;

    for(int i = 0; i <= listIndex; i++) //For each letter in template word
    {
      char[] templateChars = templateWord.toCharArray();
      while(templateChars[i] <= 122)
      {
        templateChars[i]++; //Change letter to next smallest letter in lexicographic order
        testWord = String.valueOf(templateChars);
        testWordIndex = getWordIndex(testWord,targetList);

        if(testWordIndex >= 0)
        {
          edgeNode = (WordNode) targetList.get(testWordIndex);
          nxtNode.edgeLists[1].add(edgeNode);
          edgeNode.edgeLists[1].add(nxtNode);
        }
      }
    }
  }

  //***********************************
  //String[] initialWords: An array of words to begin the path with
  //String[] targetWords: An array of words to generate paths to
  //String[] args: Array of arguments from main()
  //int argCount: The number of command line arguments
  //This method stores the arguments in these arrays for future processing
  //***********************************

  private static void storeArgs(String[] initialWords, String[] targetWords, String[] args, int argCount)
  {
    int nxtIndex = 0;

    for(int i=1; i<argCount;i++)
    {
      if(i%2 == 0)
      {
        targetWords[nxtIndex] = args[i];
        nxtIndex ++;
      }
      else
      {
        initialWords[nxtIndex] = args[i];
      }
    }
  }

  //***********************************
  //String templateWord: Starting word
  //String targetWord: Word to generate a path towards
  //This method will find the shortest path by calling findShortestPath()
  //This method calls printPath to display the results.
  //***********************************

  private void routeToTarget(String templateWord, String targetWord)
  {

    int templateLength = templateWord.length();
    int targetLength = targetWord.length();
    int templateWordIndex;
    int targetWordIndex;
    ArrayList templateList = wordBank[templateLength-1];
    ArrayList targetList = wordBank[targetLength-1];
    EdgeComparator comparator;
    PriorityQueue<WordNode> priorityQueue;
    WordNode templateNode;
    WordNode resultNode;

    templateWordIndex = getWordIndex(templateWord, templateList);
    targetWordIndex = getWordIndex(targetWord, targetList);

    if(templateWordIndex < 0 || targetWordIndex < 0)
    {
      if(templateWordIndex < 0)
      {
        System.out.println("'" + templateWord + "' is not in the dictionary.");
      }

      if(targetWordIndex < 0)
      {
        System.out.println("'" + targetWord + "' is not in the dictionary.");
      }
      return;
    }

    comparator = new EdgeComparator();
    priorityQueue = new PriorityQueue<>(64, comparator);
    templateNode = (WordNode) templateList.get(templateWordIndex);

    resultNode = findShortestPath(templateNode, priorityQueue, targetWord);

    if(resultNode != null)
    {
      printPath(resultNode);
    }
    else
    {
      System.out.println("NO POSSIBLE PATH: " + templateWord + ", " + targetWord);
    }
  }

  //***********************************
  //WordNode resultNode: the last node in the path, contains references to parent nodes
  //Prints out the path found.
  //***********************************

  private void printPath(WordNode resultNode)
  {
    int pathLength = resultNode.costSoFar + 1;
    WordNode nxtNode = resultNode;
    WordNode nodeParent;
    String[] pathResults = new String[pathLength];

    for(int i=pathLength-1; i >= 0; i--)
    {
      pathResults[i] = nxtNode.getWord();
      nodeParent = nxtNode.pathParent;
      nxtNode = nodeParent;
    }

/*TODO remove if statement for default behavior*/
    if(pathLength == 6 || pathLength == 7)
    {
      for(int j= 0; j < pathLength; j++)
      {
        System.out.print(pathResults[j] + " ");
      }

    }

    System.out.print(System.lineSeparator());
  }

  //***********************************
  //String targetWord - the word you want to find
  //Arraylist targetList - the list that you want to search
  //Return value: the index in 'targetList' where the 'targetWord' was found.
  //Uses binary search to find index:
  //Found this implementation at: http://algs4.cs.princeton.edu/11model/BinarySearch.java.html
  //***********************************

  private int getWordIndex(String targetWord, ArrayList targetList)
  {
    int listSize = targetList.size();
    int low = 0;
    int high = listSize-1;
    int comparisonVal;
    WordNode test;
    String testWord;

    while(low <= high)
    {
      int mid = low + (high-low)/2;
      test = (WordNode) targetList.get(mid);
      testWord = test.getWord();
      comparisonVal = targetWord.compareTo(testWord);

      if(comparisonVal < 0) //Test word has higher lexicographic order
      {
        high = mid -1;
      }
      else if(comparisonVal > 0) //Test word has lower lexicographic order
      {
        low = mid + 1;
      }
      else
      {
        return mid;
      }
    }

    return -1;
  }

  //***********************************
  //String a: One of two words to compute the Levenshtein distance between
  //String b: One of two words to compute the Levenshtein distance between
  //Calculates the Levenshtein distance between two words
  //https://rosettacode.org/wiki/Levenshtein_distance#Java
  //***********************************

  private static int calculateLevDistance(String a, String b) {

    int [] costs = new int [b.length() + 1];

    for (int j = 0; j < costs.length; j++)
    {
      costs[j] = j;
    }

    for (int i = 1; i <= a.length(); i++)
    {

      costs[0] = i;
      int nw = i - 1;
      for (int j = 1; j <= b.length(); j++)
      {
        int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
        nw = costs[j];
        costs[j] = cj;
      }
    }
    return costs[b.length()];
  }

  //***********************************
  //WordNode templateNode: The starting point of the path.
  //PriorityQueue priorityQueue: The priority queue to store edges in and use A* algorithm.
  //String targetWord: The word to find a path to.
  //finds the shortest path between the word in the templateNode and the targetWord
  //Returns a WordNode containing the targetWord and references to the preceding node in the path.
  //***********************************

  private WordNode findShortestPath(WordNode templateNode, PriorityQueue priorityQueue, String targetWord)
  {
    ArrayList<WordNode> targetList;
    int costSoFar;
    int levDistance;
    String edgeWord;
    boolean doneSearching = false;

    resetWordBank();

    while(!doneSearching) {

      costSoFar = templateNode.costSoFar + 1;
      templateNode.visited = true;

      for (int i = 0; i < 3; i++) {
        targetList = templateNode.edgeLists[i];
        for (WordNode nxtEdge : targetList) {
          if (!nxtEdge.visited && !nxtEdge.frontier) //If this edge has not already been visited and is not on the frontier.
          {
            edgeWord = nxtEdge.getWord();
            nxtEdge.pathParent = templateNode;
            nxtEdge.costSoFar = costSoFar;
            levDistance = calculateLevDistance(edgeWord, targetWord);
            if (levDistance == 0) {
              templateNode = nxtEdge;
              doneSearching = true;
              break;
            }

            nxtEdge.distanceToTarget = levDistance;
            nxtEdge.frontier = true;
            priorityQueue.add(nxtEdge);
          }
        }
        if (doneSearching) {
          break;
        }
      }

      if (doneSearching) {
        break;
      }

      templateNode = (WordNode) priorityQueue.poll();
      if (templateNode == null) {
        break;
      }
    }

    if(doneSearching)
    {
      return templateNode;
    }

    return null;
  }

  //***********************************
  //Resets the word bank for the next pair of words
  //No output.
  //***********************************

  private void resetWordBank()
  {
    ArrayList<WordNode> targetList;
    for(int i = 0; i < MAXWORDLENGTH; i++)
    {
      targetList = wordBank[i];

      for (WordNode nxtNode : targetList)
      {
        nxtNode.costSoFar = 0;
        nxtNode.frontier = false;
        nxtNode.visited = false;
      }
    }
  }


  public static void main(String[] args)
  {

    int argCount = args.length;
    int wordPairs = (argCount -1)/2;
    String[] initialWords = new String[wordPairs];
    String[] targetWords = new String[wordPairs];

    if((argCount%2) != 1 || argCount < 3) //TO DO:  Create proper exit
    {
      System.out.println("Invalid number of arguments");
    }

    String dictionaryPath = args[0];

    storeArgs(initialWords, targetWords, args, argCount);

    WordPath pathFinder = new WordPath();
    pathFinder.buildWordBank();
    pathFinder.processDictionary(dictionaryPath);
    pathFinder.createEdgeLists();

    for(int i= 0; i < wordPairs; i++)
    {
      pathFinder.routeToTarget(initialWords[i], targetWords[i]);
    }
  }
}


