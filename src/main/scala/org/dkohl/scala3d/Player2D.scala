package org.dkohl.scala3d

class Player2D(val pos: Vector2D[Double], val dir: Vector2D[Double], val camPlane: Vector2D[Double]) {

    import math._

    val gridPos: Vector2D[Int] = Vector2D(pos.x.toInt, pos.y.toInt)

    def collision(w: WorldMap2D) = w.collids(gridPos.x, gridPos.y)     

    def fwd(by: Double, w: WorldMap2D): Player2D = {
        val p = new Player2D(
            Vector2D(pos.x + dir.x * by, pos.y + dir.y * by),
            dir,
            camPlane
        )
        if(p.collision(w)) this else p
    }

    def bwd(by: Double, w: WorldMap2D): Player2D = {
        val p = new Player2D(
            Vector2D(pos.x - dir.x * by, pos.y - dir.y * by),
            dir,
            camPlane
        )
        if(p.collision(w)) this else p
    }

    def right(by: Double): Player2D = 
        new Player2D(
            pos,
            Vector2D(
                dir.x * cos(-by) - dir.y * sin(-by),
                dir.x * sin(-by) + dir.y * cos(-by)
            ),
            Vector2D(
                camPlane.x * cos(-by) - camPlane.y * sin(-by),
                camPlane.x * sin(-by) + camPlane.y * cos(-by)
            )
        )

    def left(by: Double): Player2D = 
        new Player2D(
            pos,
            Vector2D(
                dir.x * cos(by) - dir.y * sin(by),
                dir.x * sin(by) + dir.y * cos(by)
            ),
            Vector2D(
                camPlane.x * cos(by) - camPlane.y * sin(by),
                camPlane.x * sin(by) + camPlane.y * cos(by)
            )
        )

}