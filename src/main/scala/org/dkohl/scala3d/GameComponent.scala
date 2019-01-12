package org.dkohl.scala3d

import javax.swing.JComponent
import java.awt.{Graphics, Graphics2D, Color}

class GameComponent(controller: GameController, val raycasting: RayCasting, val textures: Array[Texture] = Array.empty[Texture]) extends JComponent {

    override def paintComponent(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]
        g2d.setColor(new Color(50, 50, 50))
        g2d.fillRect(0, 0, getWidth, getHeight / 2)  
        g2d.setColor(new Color(80, 80, 80))
        g2d.fillRect(0, getHeight / 2, getWidth, getHeight) 

        val rendering = 
            if(textures.isEmpty) raycasting.untextured(getWidth, getHeight, controller.player)
            else raycasting.textured(getWidth, getHeight, controller.player, textures)
        rendering.foreach(
            line => line.paint(g2d)
       )
    }

}