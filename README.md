# Scala3D
A simple 3D engine similar to Wolfenstein 3D.
The left view shows the rendered 3d and the left view a 2d map with the rays from ray casting painted in red.
The image plane is shown in green.

![scala3d](https://raw.githubusercontent.com/dkohlsdorf/Scala3D/master/scala3d.png)

In order to start the game run: `sbt run` and you can move with `WASD`.

The player implementation as well as the movement and rotation are implemented in [Player](https://github.com/dkohlsdorf/Scala3D/blob/master/src/main/scala/org/dkohl/scala3d/Player2D.scala).
The raycaster is implemented in [RayCasting](https://github.com/dkohlsdorf/Scala3D/blob/master/src/main/scala/org/dkohl/scala3d/RayCasting.scala)
