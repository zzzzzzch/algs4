import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private SearchNode currentNode;    
	private SearchNode twincurrentNode;  //add a twin of currentNode to see the currentNode solvable
	private Stack<Board> solution; //build a stack to store and output the procedure

	private class SearchNode implements Comparable<SearchNode> {
		public Board board;
		public int moves;
		public SearchNode preSearchNode;

		public final int priority;

		public SearchNode(Board inboard, SearchNode inPreSearchNode) {
			board = inboard;
			preSearchNode = inPreSearchNode;

			if (inPreSearchNode == null) {
				moves = 0;
			} else {
				moves = inPreSearchNode.moves + 1;
			}
			
			priority = moves + board.manhattan();
		}
		
		@Override
		public int compareTo(SearchNode o) {
			return Integer.compare(this.priority, o.priority);
	    }
		
	}

	public Solver(Board initial) {
		if (initial == null) {
			throw new IllegalArgumentException("Constructor argument Board is null!");
		}

		currentNode = new SearchNode(initial, null);
		twincurrentNode = new SearchNode(initial.twin(), null);
		MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
		MinPQ<SearchNode> twinPriorityQueue = new MinPQ<SearchNode>();

		priorityQueue.insert(currentNode);
		twinPriorityQueue.insert(twincurrentNode);

		while(true) {
			currentNode = priorityQueue.delMin();
			if (currentNode.board.isGoal()) {
				break;
			}
			putNeighBorsIntoPQ(currentNode, priorityQueue);

			twincurrentNode = twinPriorityQueue.delMin();
			if (twincurrentNode.board.isGoal()) {
				break;
			}
			putNeighBorsIntoPQ(twincurrentNode, twinPriorityQueue);
		}
	}

	private void putNeighBorsIntoPQ(SearchNode a, MinPQ<SearchNode> pq) {
		Iterable<Board> neighbors = a.board.neighbors();
		for (Board neighbor : neighbors) {
			if (a.preSearchNode == null || !neighbor.equals(a.preSearchNode.board)) {
				pq.insert(new SearchNode(neighbor, a));
			}
		}
	}

	public boolean isSolvable() {
		return currentNode.board.isGoal();
	}

	public int moves() {
		if (currentNode.board.isGoal()) {
			return currentNode.moves;
		} else {
			return -1;
		}
	}

	public Iterable<Board> solution() {
		if (currentNode.board.isGoal()) {
			solution = new Stack<Board>();
			//SearchNode node = currentNode;  // do not need to create a "node"
			while (currentNode != null) {
				solution.push(currentNode.board);
				currentNode = currentNode.preSearchNode;
			}
			return solution;
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]); 
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }



}