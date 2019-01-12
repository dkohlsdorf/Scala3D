package org.dkohl.scala3d
import scala.io.Source

class WorldMap2D(x: Vector[Int], val cols: Int) {

    def apply(i: Int, j: Int): Int = x(i * cols + j)

    def collids(i: Int, j: Int): Boolean = 
        apply(i, j) != WorldMap2D.EMPTY

    def rows = x.size / cols

} 

object WorldMap2D {

    final val EMPTY = 0

    def parseLine(line: String, sep: Char): Array[Int] = 
        line.split(sep).map(_.toInt)

    def apply(file: String, sep: Char): WorldMap2D = {
        val grid = Source.fromFile(file).getLines.map(x => parseLine(x, sep)).toVector
        new WorldMap2D(grid.flatten, grid(0).length)
    }
    
}