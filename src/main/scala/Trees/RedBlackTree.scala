package Trees

import scala.annotation.tailrec

/**
  * An implementation of a Red-Black tree for computing order statistics
  * This implementation follows "Red-Black Trees in a Functional Setting" by Chris Okasaki, 1993
  */
object RedBlackTree {
	val B: Boolean = false
	val R: Boolean = true
	
	/**
	  * Abstract for the EmptyTree and Tree classes
	  */
	object RedBlackTree {}
	sealed abstract class RedBlackTree {
		val isEmpty: Boolean
		val size: Int
		def insert(a: Int): Tree
		def ins(a: Int): Tree
		override def toString: String
		def balance: RedBlackTree
	}
	
	/**
	  * The empty tree object (these are all black, but who cares)
	  */
	case object EmptyTree extends RedBlackTree {
		val isEmpty: Boolean = true
		val size: Int = 0
		override def insert(a: Int): Tree = Tree(a)
		override def ins(a: Int): Tree = Tree(a)
		override def toString: String = ""
		override def balance: RedBlackTree = EmptyTree
	}
	
	/**
	  * Factory for the tree object
	  */
	object Tree {
		/**
		  * A single value factory method
		  * @param value the value of the new tree node
		  * @return a tree with two empty children whose value is provided
		  */
		def apply(value: Int): Tree = new Tree(R, EmptyTree, value, EmptyTree)
		
		/**
		  * Constructs a Red-Black tree from a list
		  * @param list the list of integers to add
		  * @return a tree with all the values in it
		  */
		def apply(list: List[Int]): Tree = {
			@tailrec
			def acc(toAdd: List[Int], tree: Tree): Tree = toAdd match {
				case Nil => tree
				case h :: tail => acc(tail, tree.insert(h))
			}
			
			acc(list.tail, Tree(list.head))
		}
	}
	
	/**
	  * A case class for the Tree
	  * @param color the color of the tree node
	  * @param left the left subtree
	  * @param value the value being stored
	  * @param right the right subtree
	  */
	case class Tree(color: Boolean, left: RedBlackTree, value: Int, right: RedBlackTree) extends RedBlackTree {
		/** Is this the empty tree? No */
		val isEmpty: Boolean = false
		
		/** The size of the sbutree rooted at this node */
		lazy val size: Int = left.size + right.size + 1
		
		/**
		  * Meant to be called at the root of the tree. Inserts an element into the tree
		  * @param a the value to insert
		  * @return a new tree (root) with the value inserted
		  */
		override def insert(a: Int): Tree = this.ins(a).makeBlack
		
		/**
		  * Places a value in the sub tree
		  * @param a the value to insert
		  * @return a new tree with the added value
		  */
		override def ins(a: Int): Tree = this match {
			case Tree(c, l, v, r) =>
				if (a <= v) Tree(c, l.ins(a), v, r).balance
				else Tree(c, l, v, r.ins(a)).balance
		}
		
		/**
		  * Returns a new tree with same values and children but with color black
		  * @return a black tree
		  */
		def makeBlack: Tree = this match {
			case Tree(_, a, x, b) => Tree(B, a, x, b)
		}
		
		/**
		  * Returns a new balanced tree
		  * @return a balanced tree
		  */
		override def balance: Tree = this match {
			case Tree(B, Tree(R, Tree(R, a, x, b), y, c), z, d) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(B, Tree(R, a, x, Tree(R, b, y, c)), z, d) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(B, a, x, Tree(R, Tree(R, b, y, c), z, d)) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(B, a, x, Tree(R, b, y, Tree(R, c, z, d))) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(c, a, x, b) => Tree(c, a, x, b)
		}
		
		/**
		  * Provides ability to print colors for trees
		  * @param c the color
		  * @return The "name" of the color
		  */
		private def colorString(c: Boolean): String = if (color == R) "R" else "B"
		
		/**
		  * Returns a string representation of the tree
		  * @return tree in string form
		  */
		override def toString: String = this match {
			case Tree(c, EmptyTree, v, EmptyTree) => s"$v${colorString(c)}"
			case Tree(c, l, v, r) => s"$v${colorString(c)}[${l.toString}|${r.toString}]"
		}
	}
	
	/**
	  * Returns the kth order statistic
	  * @param k the k in kth
	  * @param tree the tree on which you wish to compute this
	  * @return the desired value
	  */
	@tailrec
	def kthOrder(k: Int, tree: RedBlackTree): Int = tree match {
		case Tree(_, l, v, r) =>
			if (k < tree.size) kthOrder(k, l)
			else if (k > tree.size) kthOrder(k, r)
			else v
		case EmptyTree => throw new Error("Index out of bound")
	}
}
