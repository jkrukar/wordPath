//***********************************
//John Krukar
//This class is used as the nodes in the undirected graph generated to find the shortest path between nodes using A*
//It stores the edges between itself and other nodes in three arraylists categorized by length.
//There is an arraylist for node connections with words shorter, longer, and of equal length to the word in containing node.
//***********************************

import java.util.ArrayList;

public class WordNode
{
  private String name;
  protected ArrayList[] edgeLists;
  private ArrayList<WordNode> shorter;
  private ArrayList<WordNode> same;
  private ArrayList<WordNode> longer;
  protected boolean frontier;
  protected boolean visited;
  protected int costSoFar;
  protected int distanceToTarget;
  protected WordNode pathParent;

  public WordNode(String word)
  {
    this.name = word;
    this.edgeLists = new ArrayList[3];
    this.shorter = new ArrayList<WordNode>();
    this. same = new ArrayList<WordNode>();
    this.longer = new ArrayList<WordNode>();
    this.edgeLists[0]=shorter;
    this. edgeLists[1]=same;
    this.edgeLists[2]=longer;
    frontier = false;
    visited = false;
    costSoFar = 0;
    distanceToTarget = 0;

  }

  //***********************************
  //Output: String word: The word represented by the WordNode
  // The word is a 1-12 letter String
  // This method returns the word in the WordNode object
  //***********************************

  public String getWord()
  {
    return this.name;
  }

}
