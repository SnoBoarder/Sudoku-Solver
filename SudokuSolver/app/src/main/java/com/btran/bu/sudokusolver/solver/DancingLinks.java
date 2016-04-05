package com.btran.bu.sudokusolver.solver;

import com.btran.bu.sudokusolver.util.SudokuUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Sudoku Dancing Links is an implementation of resolving the Exact Cover Problem, in this case, Sudoku.
 * Dancing Links algorithm takes the Exact Cover matrix and puts it into a toroidal doubly-linked list.
 * Every column has a special HeaderNode that contains information about the amount of Nodes are within a given column.
 * Each Node points to another Node/HeaderNode up, down, left, and right and also has a reference to its HeaderNode.
 */
public class DancingLinks
{
                                                      // position + row                      + column                         + box
    private final static int TOTAL_MATRIX_COLS = 324; // 81 cells + (9 rows * 9 digits rows) + (9 columns * 9 digits columns) + (9 columns * 9 digits box)
    private final static int TOTAL_MATRIX_ROWS = 729; // 81 * 9

    // index into the sections of the matrix
    private final static int START_OF_ROW_COLUMNS = 81 * 1;
    private final static int START_OF_COL_COLUMNS = 81 * 2;
    private final static int START_OF_BOX_COLUMNS = 81 * 3;

    // the root header (used to keep track of the columns)
    private HeaderNode _root;
    // permanent reference to every column header
    List<HeaderNode> _cols;
    // permanent reference to the STARTING NODE of every row (in this case,
    // the node that represents the cell position)
    private List<Node> _rows;

    // the solution that is push'd and pop'd according to the backtracking algorithm
    Stack<Node> _solutions = new Stack<Node>();

    // member reference to the cells that need to be populated
    private int[] _cells;

    // debug member variable to know how deep the search algorithm goes for a given sudoku board.
    private int _maxDepth = 0;

    private int _actions = 0;

    /**
     * Initialize the matrix root, headers, and nodes.
     * This populates every single possible scenario of the Sudoku game.
     */
    public DancingLinks()
    {
        // initialize links
        _root = new HeaderNode();
        _root.left = _root;
        _root.right = _root;

        // create the headers
        _cols = new ArrayList<HeaderNode>(TOTAL_MATRIX_COLS);
        HeaderNode curr = _root;
        HeaderNode next = null;
        for (int i = 0; i < TOTAL_MATRIX_COLS; ++i)
        {
            // create header and insert them into the linked list from left to right
            next = new HeaderNode();
            _cols.add(next);
            curr.right = next;
            next.left = curr;

            if (curr != _root)
            { // set up and down for all headers except the root
                curr.up = curr;
                curr.down = curr;
            }

            curr = next;
        }
        // finish wrapping the last node around
        _root.left = next;
        next.right = _root;

        // make sure the last header is set properly
        next.up = next;
        next.down = next;

        // create matrix of all possible sudoku positions
        // Each set of columns within the matrix represent different constraints of the Sudoku Board:
        // 0-80: a position constraint: Only 1 number can occupy a cell
        // 81-161: a row constraint: Only 1 instance of a given number can be in the row
        // 162-242: a column constraint: Only 1 instance of a number can be in a column
        // 243-323: a region constraint: Only 1 instance of a number can be in a box region.
        // There are 324 total columns and 729 (81 cells * 9 values) within the matrix.
        _rows = new ArrayList<Node>(TOTAL_MATRIX_ROWS);
        for (int i = 0; i < 81; ++i)
        {
            for (int value = 1; value <= 9; ++value)
            {
                int row = i / 9;
                int col = i % 9;
                int box = ((i / 27) * 3) + ((i % 9) / 3);

                int rowIndex = START_OF_ROW_COLUMNS + (row * 9 + value - 1); // "value - 1" to make it base 0
                int colIndex = START_OF_COL_COLUMNS + (col * 9 + value - 1);
                int boxIndex = START_OF_BOX_COLUMNS + (box * 9 + value - 1);
                addRow(value, i, rowIndex, colIndex, boxIndex); // adds a row to the matrix and to the _rows array
            }
        }
    }

    /**
     * Helper function to generate the row within the matrix. Place "1's" within the matrix accordingly.
     *
     * @param value Cell value
     * @param index Position of cell
     * @param rowIndex Row of the cell
     * @param colIndex Column of the cell
     * @param boxIndex Box region of the cell
     */
    private void addRow(int value, int index, int rowIndex, int colIndex, int boxIndex)
    {
        HeaderNode header;
        Node start;
        Node prev;

        // get column for the appropriate indices

        // set first node of the row to define the matrix's position
        header = _cols.get(index);
        start = createNode(null, header);
        start.index = index;
        start.value = value;
        _rows.add(start); // only need to store the reference to the first item

        // set rowindex of the row
        header = _cols.get(rowIndex);
        prev = createNode(start, header);
        prev.index = index;
        prev.value = value;

        // set colIndex of the row
        header = _cols.get(colIndex);
        prev = createNode(prev, header);
        prev.index = index;
        prev.value = value;

        // set boxIndex of the row
        header = _cols.get(boxIndex);
        prev = createNode(prev, header);
        prev.index = index;
        prev.value = value;

        // loop the row
        start.left = prev;
        prev.right = start;
    }

    /**
     *
     * @param last last node within the chain
     * @param head the head of the chain (special node that is not part of the list)
     * @return newly created node
     */
    private Node createNode(Node last, HeaderNode head)
    {
        Node newNode = new Node();

        // set left/right connections
        if (last != null)
        {
            newNode.left = last;
            newNode.right = last.right;
            newNode.left.right = newNode;
            newNode.right.left = newNode;
        }
        else
        { // first in the row, refer to itself
            newNode.left = newNode;
            newNode.right = newNode;
        }

        // set up/down connections
        newNode.header = head;
        ++head.count;
        newNode.down = head;
        newNode.up = head.up;
        newNode.up.down = newNode;
        newNode.down.up = newNode;

        return newNode;
    }

    /**
     * Loads a given sudoku board, searches for the solution, and then populates the cells from the solution.
     *
     * @param cells Given Sudoku board that will be populated once a solution is found
     */
    public void loadAndSearch(int[] cells)
    {
        // store the reference to the cells so that the search algorithm can update it when the solution is found.
        _cells = cells;

        loadData(_cells); // load given sudoku board
        search(0); // search AND populate cells once found.
    }

    /**
     * Goes through all given cells and removing all columns that relate to the given cells.
     * The algorithm will be prepared to run once the known columns and rows are removed.
     * @param cells The given sudoku board
     */
    private void loadData(int[] cells)
    {
        int value;
        int len = cells.length;

        for (int i = 0; i < len; ++i)
        {
            value = cells[i];
            if (value == SudokuUtil.EMPTY_CELL)
                continue;

            int row = i / 9;
            int col = i % 9;
            int box = ((i / 27) * 3) + ((i % 9) / 3);

            int rowIndex = START_OF_ROW_COLUMNS + (row * 9 + value - 1); // "value - 1" to make it base 0
            int colIndex = START_OF_COL_COLUMNS + (col * 9 + value - 1);
            int boxIndex = START_OF_BOX_COLUMNS + (box * 9 + value - 1);

            // remove the 4 columns as an option
            cover(_cols.get(i));
            cover(_cols.get(rowIndex));
            cover(_cols.get(colIndex));
            cover(_cols.get(boxIndex));

            // NOTE: Do not need to store this value into the solution since it's already in the cell list!
        }
    }

    /**
     * The main algorithm that finds the correct solution set.
     * It uses the backtracking method in combination with the exact cover method.
     * If a "cover" of a given column fails, it backtracks by "uncover"ing the column.
     * @param depth Debug information of what recursion depth the search method is on.
     */
    private void search(int depth)
    {
        if (depth > _maxDepth)
            _maxDepth = depth;

        if (_root.right == _root)
        { // all columns have been used. we've found a solution!

            // take the solution and get the index and value information
            Node[] solution = new Node[_solutions.size()];
            _solutions.toArray(solution);
            Node node;
            int len = _solutions.size();
            for (int i = 0; i < len; ++i)
            {
                node = solution[i];
                _cells[node.index] = node.value;

                ++_actions;
            }
            return;
        }

        // choose the next column to cover based on size prioirty (lower amount of rows in a column means less branching)
        HeaderNode column = chooseNextColumn();
        cover(column);

        for (Node rowNode = column.down; rowNode != column; rowNode = rowNode.down)
        {
            // traverse every row until we loop around
            _solutions.push(rowNode);

            for (Node rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right)
            {
                // traverse and cover every node going right as long as we're not the current node
                cover(rightNode.header);

                ++_actions;
            }

            search(depth + 1);

            // backtrack this step
            _solutions.pop();

            for (Node leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left)
            {
                // traverse and uncover every node going left as long as we're not the current node
                uncover(leftNode.header);

                ++_actions;
            }
        }

        uncover(column);
    }

    /**
     * Removes a column from the matrix including all rows that connect to this column.
     * @param column Reference to the header of a specified column
     */
    private void cover(HeaderNode column)
    {
        // remove the current column from the other columns
        column.right.left = column.left;
        column.left.right = column.right;

        ++_actions;

        for (Node rowNode = column.down; rowNode != column; rowNode = rowNode.down)
        {
            // traverse down the column
            for (Node rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right)
            {
                // remove the row by traversing the entire row from left to right and remove references accordingly
                rightNode.up.down = rightNode.down;
                rightNode.down.up = rightNode.up;

                // decrement header count
                --rightNode.header.count;

                ++_actions;
            }
        }
    }

    /**
     * Adds a column back into the matrix, including all rows that connected to the column.
     * This method works because the nodes from the removed column retained its information about its neighbors.
     * This is an exact reversal of what cover() does.
     * @param column Reference to the header of a specified column
     */
    private void uncover(HeaderNode column)
    {
        for (Node rowNode = column.up; rowNode != column; rowNode = rowNode.up)
        {
            for (Node leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left)
            {
                // attach the reference back to this node by using the data the node already had to reattach
                leftNode.up.down = leftNode;
                leftNode.down.up = leftNode;

                // increment header count
                ++leftNode.header.count;

                ++_actions;
            }
        }

        // add the column back into the list
        column.right.left = column;
        column.left.right = column;

        ++_actions;
    }

    /**
     * Choose the column with the least amount of nodes. This decreases the branching algorithm.
     * @return The column
     */
    private HeaderNode chooseNextColumn()
    {
        Node targetCol = null;
        int lowestCount = TOTAL_MATRIX_ROWS; // set to max possible rows for a given column

        for (Node node = _root.right; node != _root; node = node.right)
        {
            // check all columns and keep track of the column with the lowest count
            int count = ((HeaderNode)node).count;
            if (count < lowestCount)
            {
                lowestCount = count;
                targetCol = node;
            }

            ++_actions;
        }

        return (HeaderNode)targetCol;
    }

    /**
     * The total amount of actions needed to complete the dancing links solution
     * @return the total amount of actions
     */
    public int getActionCount()
    {
        return _actions;
    }

    /**
     * The max depth level the algorithm took to find the solution
     * @return the max depth level
     */
    public int getMaxDepthLevel()
    {
        return _maxDepth;
    }

    /**
     * Nodes represent the matrix data. If a node exists, it's considered "1" within the row/column.
     * If a node doesn't exist, it's considered "0" within the row/column.
     */
    class Node
    {
        // reference to the header node
        public HeaderNode header;

        // reference to other nodes within the same column
        public Node up;
        public Node down;

        // reference to other nodes within the same row
        public Node right;
        public Node left;

        // reference to the data this node represents (created for easy access of information)
        public int index;
        public int value;
    }

    /**
     * Header node is the head of every column
     */
    class HeaderNode extends Node
    {
        public int count;
    }
}






