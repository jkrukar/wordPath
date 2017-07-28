//***********************************
//John Krukar
//A comparator for the edge priority queue
//***********************************

import java.util.Comparator;

public class EdgeComparator implements Comparator<WordNode>
{

  //***********************************
  //The comparator sorts the nodes based on the A* heuristic (costSoFar + Levenshtein distance)
  //This method returns zero if the objects are equal.
  //It returns a positive value if node1 is greater than node2. Otherwise, a negative value is returned.
  //***********************************
  @Override
  public int compare(WordNode node1, WordNode node2) {

    int node1Distance = node1.costSoFar + node1.distanceToTarget;
    int node2Distance = node2.costSoFar + node2.distanceToTarget;

    if(node1Distance < node2Distance)
    {
      return -1;
    }

    if(node1Distance > node2Distance)
    {
      return 1;
    }
    return 0;
  }
}
