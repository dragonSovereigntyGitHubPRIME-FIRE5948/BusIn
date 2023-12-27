package com.example.sgbusandlocationalarm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


private var mapView: MapView? = null

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        mapView = findViewById(R.id.mapView)

        mapView?.getMapboxMap()?.loadStyle(
            style(styleUri = Style.MAPBOX_STREETS) {
                +circleLayer(layerId = "1", sourceId = "1") {
                    circleRadius(20000.0)
                    circleColor(Color.RED)
                    circleOpacity(1.0)
                    circleStrokeColor(Color.RED)
                }
            },

            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    addAnnotationToMap()
//                    val layer = CircleLayer("1", "1")
//                    layer.circleRadius(50.0)
//                    layer.circleOpacity(.4f)
//                    layer.circleColor(Color.WHITE)
                }
            }
        )

//        val circleManager = CircleManager(mapView, mapboxMap, style)
//        val options = CircleOptions()
//        options.(LatLng(location.getLatitude(), location.getLongitude()))
//        options.radius(500.0)
////        options.(0.5f)
//        options.fillColor(Color.RED)
//
//        circleManager.create(options)
    }

    private fun addAnnotationToMap() {
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        val annotationApi = mapView?.annotations
        createRadius(annotationApi, 100.0)
        createMarker(annotationApi, 18.06, 59.31)
    }

    private fun createMarker(annotationApi: AnnotationPlugin?, longitude: Double, latitude: Double) {
        bitmapFromDrawableRes(
            this@MainActivity2,
            R.drawable.red_marker
        )?.let {

            val pointAnnotationManager = annotationApi?.createPointAnnotationManager()
            // Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                // Define a geographic coordinate.
                .withPoint(Point.fromLngLat(longitude, latitude))
                // Specify the bitmap you assigned to the point annotation
                // The bitmap will be added to map style automatically.
                .withIconImage(it)
            // Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
        }
    }

    private fun createRadius(annotationApi: AnnotationPlugin?, radius: Double) {
        val circleAnnotationManager = annotationApi?.createCircleAnnotationManager()
        // Set options for the resulting circle layer.
        val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
            // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(18.06, 59.31))
            // Style the circle that will be added to the map.
            .withCircleRadius(radius)
            .withCircleColor("#ee4e8b")
            .withCircleStrokeWidth(2.0)
            .withCircleStrokeColor("#ffffff")
            .withCircleOpacity(0.2)
        // Add the resulting circle to the map.
        circleAnnotationManager?.create(circleAnnotationOptions)
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}