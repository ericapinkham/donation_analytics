package Trees

object RedBlackTree {
	val B: Boolean = false
	val R: Boolean = true
	
	sealed abstract class RedBlackTree {
		def insert(a: Int): Tree
		def ins(a: Int): Tree
		override def toString: String
		def balance: RedBlackTree
	}
	
	case object EmptyTree extends RedBlackTree {
		override def insert(a: Int): Tree = Tree(a)
		override def ins(a: Int): Tree = Tree(a)
		override def toString: String = ""
		override def balance: RedBlackTree = EmptyTree
	}
	
	object Tree {
		def apply(value: Int): Tree = new Tree(R, EmptyTree, value, EmptyTree)
	}
	
	case class Tree(color: Boolean, left: RedBlackTree, value: Int, right: RedBlackTree) extends RedBlackTree {
		
		override def insert(a: Int): Tree = this.ins(a).makeBlack
		
		override def ins(a: Int): Tree = this match {
			case Tree(c, l, v, r) =>
				if (a <= v) Tree(c, l.ins(a), v, r).balance
				else Tree(c, l, v, r.ins(a)).balance
		}
		
		def makeBlack: Tree = this match {
			case Tree(_, a, x, b) => Tree(B, a, x, b)
		}
		
		override def balance: Tree = this match {
			case Tree(B, Tree(R, Tree(R, a, x, b), y, c), z, d) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(B, Tree(R, a, x, Tree(R, b, y, c)), z, d) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(B, a, x, Tree(R, Tree(R, b, y, c), z, d)) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(B, a, x, Tree(R, b, y, Tree(R, c, z, d))) => Tree(R, Tree(B, a, x, b), y, Tree(B, c, z, d))
			case Tree(c, a, x, b) => Tree(c, a, x, b)
		}
		
		private def colorString(c: Boolean): String = if (color == R) "R" else "B"
		override def toString: String = this match {
			case Tree(c, EmptyTree, v, EmptyTree) => s"$v${colorString(c)}"
			case Tree(c, l, v, r) => s"$v${colorString(c)}[${l.toString}|${r.toString}]"
		}
	}
}
