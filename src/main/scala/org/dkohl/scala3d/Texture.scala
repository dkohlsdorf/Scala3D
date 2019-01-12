package org.dkohl.scala3d

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.Graphics2D

class Texture(val colors: Array[Array[Color]]) {

    import math._

    def darken(color: Color, fraction: Double): Color = {
        val red   = round(max(0, color.getRed()   - 255 * fraction))
        val green = round(max(0, color.getGreen() - 255 * fraction))
        val blue  = round(max(0, color.getBlue()  - 255 * fraction))
        new Color(red.toInt, green.toInt, blue.toInt)
    }

    def getStrip(start: Int, stop: Int, length: Int, texX: Int, side: Int, height: Int): Array[Color] = (        
        for (y <- start until stop) yield {            
            val d     = math.max(y - height / 2 + length / 2, 0)
            val texY  = ((d * colors(0).size) / length) 
            if(side == 0) colors(texX)(texY) else darken(colors(texX)(texY), 0.1)
        }
    ).toArray

}

object Texture {

    def apply(img: BufferedImage): Texture = new Texture(
        (   
            for (i <- 0 until img.getWidth) yield (for(j <- 0 until img.getHeight) yield new Color(img.getRGB(i, j))).toArray
        ).toArray
    )
        
}