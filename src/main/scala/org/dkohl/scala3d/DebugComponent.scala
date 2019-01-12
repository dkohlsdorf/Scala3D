package org.dkohl.scala3d
import javax.swing.JComponent
import java.awt.{Graphics, Graphics2D, Color}
import java.awt.image.BufferedImage

class DebugComponent(controller: GameController, val raycasting: RayCasting, map: WorldMap2D, val textures: Array[BufferedImage]) extends JComponent {

    override def paintComponent(g: Graphics): Unit = {
        g.setColor(new Color(50, 50, 50))
        g.fillRect(0, 0, getWidth, getHeight)  
        paintRays(g)
        paintCam(g)
        paintGrid(g)
    }
    
    private def paintCam(g: Graphics): Unit = {
        val stepX  = getWidth / map.rows.toInt
        val stepY  = getHeight / map.cols.toInt
        g.setColor(Color.GREEN)
        g.drawLine(
            ((controller.player.pos.x + controller.player.dir.x - controller.player.camPlane.x) * stepX).toInt, 
            ((controller.player.pos.y + controller.player.dir.y - controller.player.camPlane.y) * stepY).toInt,
            ((controller.player.pos.x + controller.player.dir.x + controller.player.camPlane.x) * stepX).toInt, 
            ((controller.player.pos.y + controller.player.dir.y + controller.player.camPlane.y) * stepY).toInt
        )
        g.fillRect((controller.player.pos.x * stepX).toInt - 3, (controller.player.pos.y * stepY).toInt - 3, 6, 6)
    }

    private def paintRays(g: Graphics): Unit = {
        val stepX  = getWidth / map.rows.toInt
        val stepY  = getHeight / map.cols.toInt
        g.setColor(Color.RED)
        raycasting.debug(getWidth, getHeight, controller.player).foreach {
            case (d, r) => 
                g.drawLine(
                    (controller.player.pos.x * stepX).toInt, 
                    (controller.player.pos.y * stepY).toInt, 
                    ((controller.player.pos.x + r.x * d) * stepX).toInt, 
                    ((controller.player.pos.y + r.y * d) * stepY).toInt
                )
        }
    }
    
    private def paintGrid(g: Graphics): Unit = {
        val stepX  = getWidth / map.rows.toInt
        val stepY  = getHeight / map.cols.toInt
        for {
            i <- 0 until map.rows
            j <- 0 until map.cols
            if map.collids(i, j)
        }  {            
            g.drawImage(
                textures(map(i, j) - 1),
                i * stepX.toInt,
                j * stepY.toInt,
                stepX.toInt,
                stepY.toInt,
                null
            )
        }
    }

}