package org.dkohl.scala3d
import scala.annotation.tailrec

class RayCasting(val world: WorldMap2D) {

    import math._

    case class Hit(pos: Vector2D[Int], side: Int)

    private def ray(row: Int, width: Int, player: Player2D): Vector2D[Double] = {
        val camX = (2 * row) / width.toDouble - 1
        val rayX = player.dir.x + player.camPlane.x * camX
        val rayY = player.dir.y + player.camPlane.y * camX
        Vector2D(rayX, rayY)
    }

    private def deltaDist(ray: Vector2D[Double]): Vector2D[Double] = 
        Vector2D(abs(1.0 / ray.x), abs(1.0 / ray.y))

    private def initialStep(ray: Vector2D[Double]): Vector2D[Int] = 
        Vector2D[Int](
            if(ray.x < 0) -1 else 1, 
            if(ray.y < 0) -1 else 1
        )

    private def initialDist(ray: Vector2D[Double], delta: Vector2D[Double], grid: Vector2D[Int], player: Player2D): Vector2D[Double] = 
        Vector2D(
            if(ray.x < 0) (player.pos.x - grid.x) * delta.x else (grid.x + 1.0 - player.pos.x) * delta.x,
            if(ray.y < 0) (player.pos.y - grid.y) * delta.y else (grid.y + 1.0 - player.pos.y) * delta.y
        )

    @tailrec
    private def traceRay(
        grid: Vector2D[Int], 
        sideDist: Vector2D[Double], 
        side: Int,
        deltaDist: Vector2D[Double],
        step: Vector2D[Int], 
        hit: Boolean,
    ): Hit = 
        if(hit) Hit(grid, side)
        else sideDist match {
            case Vector2D(x, y) if x < y => 
                traceRay(
                    Vector2D[Int](grid.x + step.x, grid.y),
                    Vector2D[Double](sideDist.x + deltaDist.x, sideDist.y),
                    0,
                    deltaDist,
                    step,
                    world.collids(grid.x + step.x, grid.y)
                )
            case Vector2D(x, y) if x >= y => 
                traceRay(
                    Vector2D[Int](grid.x, grid.y + step.y),
                    Vector2D[Double](sideDist.x, sideDist.y + deltaDist.y),
                    1,
                    deltaDist,
                    step,
                    world.collids(grid.x, grid.y + step.y)
                )
        }
        
    private def projectedDistance(
        hit: Hit,
        pos: Vector2D[Double], 
        step: Vector2D[Int], 
        dir: Vector2D[Double], 
    ): Double = 
            if (hit.side == 0) (hit.pos.x - pos.x + (1 - step.x) / 2.0) / dir.x
            else               (hit.pos.y - pos.y + (1 - step.y) / 2.0) / dir.y


    def textureX(pos: Vector2D[Double], ray: Vector2D[Double], projectedDistance: Double, side: Int, textureWidth: Int): Int = {
        val wall = 
            if(side == 0) pos.y + projectedDistance * ray.y 
            else pos.x + projectedDistance * ray.x
        val wallX = wall - floor(wall)
        val textureX = 
            if(side == 0 && ray.x > 0)      textureWidth - (wallX * textureWidth) - 1
            else if(side == 1 && ray.y < 0) textureWidth - (wallX * textureWidth) - 1
            else wallX * textureWidth
        textureX.toInt
    }

    def debug(width: Int, height: Int, player: Player2D) = 
        for(w <- 0 until width) yield {
            val r = ray(w, width, player)
            val g = player.gridPos
            val d = deltaDist(r)
            val s = initialStep(r)
            val l = initialDist(r, d, g, player)
            val h = traceRay(g, l, 0, d, s, false)
            (projectedDistance(h, player.pos, s, r), r)
        }

    def textured(width: Int, height: Int, player: Player2D, textures: Array[Texture]) = 
        for(w <- 0 until width) yield {
            val r = ray(w, width, player)
            val g = player.gridPos
            val d = deltaDist(r)
            val s = initialStep(r)
            val l = initialDist(r, d, g, player)
            val h = traceRay(g, l, 0, d, s, false)
            val p = projectedDistance(h, player.pos, s, r)
            val lineHeight = height / p
            val texX = textureX(player.pos, r, p, h.side, textures(0).colors(0).size)
            val texture = textures(world(h.pos.x, h.pos.y) - 1)
            val start = max(-lineHeight / 2 + height / 2, 0.0).toInt
            val stop  = min( lineHeight / 2 + height / 2, height - 1).toInt
            val strip = texture.getStrip(start, stop, lineHeight.toInt, texX, h.side, height)  
            TextureSliceRendering(w, start, strip)                      
        }

    def untextured(width: Int, height: Int, player: Player2D) = 
        for(w <- 0 until width) yield {
            val r = ray(w, width, player)
            val g = player.gridPos
            val d = deltaDist(r)
            val s = initialStep(r)
            val l = initialDist(r, d, g, player)
            val h = traceRay(g, l, 0, d, s, false)
            val p = projectedDistance(h, player.pos, s, r)
            val lineHeight = height / p
            LineRendering(
                w, 
                max(-lineHeight / 2 + height / 2, 0.0).toInt,
                min( lineHeight / 2 + height / 2, height - 1).toInt,
                h.side,
                world(h.pos.x, h.pos.y)
            )
        }
        

}