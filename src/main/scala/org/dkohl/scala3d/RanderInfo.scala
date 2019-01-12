package org.dkohl.scala3d

import java.awt.Color
import java.awt.Graphics2D

trait RenderInfo {

    def paint(g2d: Graphics2D): Unit

}

case class TextureSliceRendering(row: Int, start: Int, colors: Array[Color]) extends RenderInfo {

    override def paint(g2d: Graphics2D): Unit = 
        for(i <- 0 until colors.size) {
            g2d.setColor(colors(i))
            g2d.fillRect(row, start + i, 1, 1)
        }

}

case class LineRendering(row: Int, start: Int, stop: Int, side: Int, wallType: Int) extends RenderInfo {

    final val COLOR: Color = wallType match {
        case 1 => if(side == 0) Color.RED   else new Color(255 / 2, 0, 0)
        case 2 => if(side == 0) Color.GREEN else new Color(0, 255 / 2, 0)
        case 3 => if(side == 0) Color.BLUE  else new Color(0, 0, 255 / 2)
        case 4 => if(side == 0) Color.WHITE else new Color(255 / 2, 255 / 2, 255 / 2)
        case _ => Color.YELLOW
    }

    override def paint(g2d: Graphics2D): Unit = {
        g2d.setColor(COLOR)
        g2d.drawLine(row, start, row, stop)
    }

}