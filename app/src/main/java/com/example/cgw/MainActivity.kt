package com.example.cgw

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.cgw.ui.theme.ExtendedTheme
import com.example.cgw.ui.theme.CGWTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    companion object {
        const val BALL_RADIUS = 50f
        const val FRICTION = 0.03f
        const val BOUNCE = 1.4f
        const val ACCEL_COEFF = 500f
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var gravitySensor: Sensor

    private var ballPos = mutableStateOf(Offset.Zero)
    private var ballSpeed = mutableStateOf(Offset.Zero)
    private var gravityValue = mutableStateOf(Offset.Zero)

    private var width: Float = 0.0f
    private var height: Float = 0.0f

    private var time = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!!

        setContent {
            CGWTheme {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dp(15f))
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.ball_accel_text,
                                gravityValue.value.x,
                                gravityValue.value.y
                            )
                        )

                        val ballColor = ExtendedTheme.colors.ballColor
                        val borderColor = ExtendedTheme.colors.borderColor
                        val vectorColor = ExtendedTheme.colors.vectorColor
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .padding(Dp(20f)), onDraw = {
                            width = this.size.width
                            height = this.size.height

                            drawCircle(ballColor, BALL_RADIUS, this.center + ballPos.value)
                            drawLine(
                                vectorColor,
                                this.center + ballPos.value,
                                this.center + ballPos.value + gravityValue.value * 20f,
                                6f
                            )

                            drawLine(borderColor, Offset.Zero, Offset(this.size.width, 0f), 6f)
                            drawLine(
                                borderColor,
                                Offset.Zero,
                                Offset(0f, this.size.height),
                                6f
                            )
                            drawLine(
                                borderColor,
                                Offset(this.size.width, 0f),
                                Offset(this.size.width, this.size.height),
                                6f
                            )
                            drawLine(
                                borderColor,
                                Offset(0f, this.size.height),
                                Offset(this.size.width, this.size.height),
                                6f
                            )

                        })
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        time = System.currentTimeMillis()
        sensorManager.registerListener(
            this,
            gravitySensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_GRAVITY) {
                gravityValue.value = Offset(-event.values[0], event.values[1])

                val newTime = System.currentTimeMillis()
                val deltaTime = ((newTime - time) / 1000f)
                time = newTime

                val absX = ballPos.value.x + width / 2
                val absY = ballPos.value.y + height / 2

                //applying acceleration to speed
                ballSpeed.value += gravityValue.value * ACCEL_COEFF * deltaTime
                //applying friction to speed
                ballSpeed.value *= (1 - FRICTION)

                ballPos.value += ballSpeed.value * deltaTime

                var newX = ballPos.value.x
                var newY = ballPos.value.y

                var newXSpeed = ballSpeed.value.x
                var newYSpeed = ballSpeed.value.y

                //check and solve left and right borders collision
                if (absX > width - BALL_RADIUS) {
                    newX = width / 2f - BALL_RADIUS
                    newXSpeed = -newXSpeed * BOUNCE
                } else if (absX < BALL_RADIUS) {
                    newX = -(width / 2f - BALL_RADIUS)
                    newXSpeed = -newXSpeed * BOUNCE
                }

                //check and solve top and bottom borders collision
                if (absY > height - BALL_RADIUS) {
                    newY = height / 2f - BALL_RADIUS
                    newYSpeed = -newYSpeed * BOUNCE
                } else if (absY < BALL_RADIUS) {
                    newY = -(height / 2f - BALL_RADIUS)
                    newYSpeed = -newYSpeed * BOUNCE
                }

                ballPos.value = Offset(newX, newY)
                ballSpeed.value = Offset(newXSpeed, newYSpeed)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    CGWTheme {
        Surface(
            modifier = Modifier
                .width(Dp(300f))
                .height(Dp(700f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dp(15f))
            ) {
                Text(
                    text = stringResource(
                        id = R.string.ball_accel_text,
                        2.35f,
                        6.86f
                    )
                )

                val ballColor = ExtendedTheme.colors.ballColor
                val borderColor = ExtendedTheme.colors.borderColor
                val vectorColor = ExtendedTheme.colors.vectorColor
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .padding(Dp(20f)), onDraw = {


                    drawCircle(
                        ballColor,
                        MainActivity.BALL_RADIUS,
                        this.center + Offset(40f, 50f)
                    )
                    drawLine(
                        vectorColor,
                        this.center + Offset(40f, 50f),
                        this.center + Offset(40f, 50f) + Offset(7f, 3f) * 20f,
                        6f
                    )

                    drawLine(borderColor, Offset.Zero, Offset(this.size.width, 0f), 6f)
                    drawLine(borderColor, Offset.Zero, Offset(0f, this.size.height), 6f)
                    drawLine(
                        borderColor,
                        Offset(this.size.width, 0f),
                        Offset(this.size.width, this.size.height),
                        6f
                    )
                    drawLine(
                        borderColor,
                        Offset(0f, this.size.height),
                        Offset(this.size.width, this.size.height),
                        6f
                    )

                })
            }
        }
    }
}