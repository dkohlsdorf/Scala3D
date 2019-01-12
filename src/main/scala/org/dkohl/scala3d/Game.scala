package org.dkohl.scala3d

import org.dkohl.scala3d._
import javax.swing.{JFrame, JPanel}
import java.awt.{Dimension, GridLayout}
import javax.imageio.ImageIO
import java.io.File

object Game {

    final val WIDTH  = 800
    final val HEIGHT = 300

    def main(args: Array[String]): Unit = {
        val world = WorldMap2D("assets/map2.csv", ',')
        val initial = new Player2D(
            Vector2D(22.0, 11.5),
            Vector2D(-1.0, 0.0),
            Vector2D( 0.0, 0.66)
        )
        val images = Array(
            ImageIO.read(new File("assets/eagle.png")),
            ImageIO.read(new File("assets/redbrick.png")),
            ImageIO.read(new File("assets/purplestone.png")),
            ImageIO.read(new File("assets/greystone.png")),
            ImageIO.read(new File("assets/bluestone.png")),
            ImageIO.read(new File("assets/mossy.png")),
            ImageIO.read(new File("assets/wood.png")),
            ImageIO.read(new File("assets/colorstone.png"))
        )        
        val textures   = images.map(i => Texture(i))  
        val raycasting = new RayCasting(world)
        val controller = new GameController(initial, world, 0.5, 0.1)
        val screen     = new GameComponent(controller, raycasting, textures)
        val debug2d    = new DebugComponent(controller, raycasting, world, images)    
        controller.cmp = Array(screen, debug2d)
    
        val layout = new GridLayout(0, 2)
        val panel  = new JPanel()
        panel.setLayout(layout)
        panel.add(screen)
        panel.add(debug2d)

        val frame = new JFrame("Scala3D")
        frame.add(panel)
        frame.addKeyListener(controller)
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT))
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.pack
        frame.setVisible(true)    
    }

}