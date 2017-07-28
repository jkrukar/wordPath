WordPath 

by: John Krukar

-Finds the shortest path between two words in the dictionary. 

-Each word in the path has a Levenshtein distance of 1 from the word preceding it.

-----------------------------
Command Line Arguments:
-----------------------------

-Accepts 2n+1 arguments.

-The first argument is a path to a dictionary relative to the JAR file.

-The other 'n' arguments are pairs of words to generate optimized paths between (ex: black - green)

-----------------------------
Class Files:
-----------------------------

-ReadTextFile.java
	
	-Used to read in words from the dictionary. (It is the demo from the CS351 class website)

-EdgeComparator.java

	-Defines a custom comparator to be used in a priority queue. It defines a heuristic to implement the A* pathfinding algorithm.

-WordNode.java

	-Defines a word node that stores a unique word in the dictionary. It also stores the connections to all adjacent nodes. An adjacent node is a node containing a word with a Levenshtein distance of 1 from the original node word.

-WordPath.java

	-Contains main()

	-Generates an optimized path between word pairs using the A* pathfinding algorithm.
