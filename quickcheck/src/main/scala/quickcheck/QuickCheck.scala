package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }
  
  property("min2") = forAll { (a: Int, b: Int) =>
    val h = insert(a, empty)
    val g = insert(b, h)
    
    val smallest = if(a <= b) {a} else {b}
    
    findMin(g) == smallest
  }
  
  property("empty1") = forAll { a: Int =>
    val h = insert(a, empty)
    val g = deleteMin(h)
    
    isEmpty(g)
  }  
  
  property("delete1") = forAll { (a: Int, b: Int) =>
    val h = insert(a, empty)
    val g = insert(b, h)
    val f = deleteMin(g)

    val greatest = if(a > b) {a} else {b}    
    
    findMin(f) == greatest
  }    
    
  property("delete2") = forAll { (a: Int, b: Int) =>
    val h = insert(a, empty)
    val g = insert(b, h)
    val f = deleteMin(g)
    val e = deleteMin(f)

    isEmpty(e)
  }  
  
 
  property("meld1") = forAll { (a: Int, b: Int) =>
    val h = insert(a, empty)
    val g = insert(b, empty)
    
    val i = meld(h, g)
    
    val smallest = if(a <= b) {a} else {b}
    
    findMin(i) == smallest
  }
  
  property("ord1") = forAll { h: H =>
   
    isOrdered(h, None)
  }
  
  property("ord2") = forAll { (h: H, g: H) =>
    val i = meld(h, g)
    
    isOrdered(i, None)
  }  
  
  def isOrdered(h: H, prev: Option[Int]) : Boolean = {
    val min = findMin(h)
    
    prev match {
      case Some(value) => if(min < value) return false
      case None => Unit
    }
    
    val g = deleteMin(h)
  
    if(isEmpty(g)){
      true;
    }
    else {
    	isOrdered(g, Some(min))
    }
}

  lazy val genHeap: Gen[H] = for {
      k <- arbitrary[Int]
      m <- oneOf(empty, genHeap, genHeap, genHeap, genHeap)
  } yield insert(k, m)
  
  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

}
