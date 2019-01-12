package org.dkohl.scala3d

import java.awt.event.{KeyListener, KeyEvent}
import javax.swing.JComponent

class GameController(var player: Player2D, world: WorldMap2D, speed: Double, rot: Double) extends KeyListener {
    
    var cmp: Array[JComponent] = Array.empty[JComponent]

    override def keyTyped(event: KeyEvent): Unit = {}

    override def keyReleased(event: KeyEvent): Unit = {}

    override def keyPressed(event: KeyEvent): Unit = {
        event.getKeyCode match {
            case KeyEvent.VK_W => player = player.fwd(speed, world)
            case KeyEvent.VK_S => player = player.bwd(speed, world)
            case KeyEvent.VK_A => player = player.left(rot)
            case KeyEvent.VK_D => player = player.right(rot)
            case _ => Unit
        }
        cmp.map(_.repaint())
    }

} 