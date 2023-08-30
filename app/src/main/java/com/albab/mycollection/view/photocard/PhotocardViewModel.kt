package com.albab.mycollection.view.photocard

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albab.mycollection.config.util.ImageConverter
import com.albab.mycollection.config.util.Rectangle
import com.albab.mycollection.domain.model.Photocard
import com.albab.mycollection.domain.repository.PhotocardRepository
import com.albab.mycollection.view.photocard.details.PhotocardDetailsUIState
import com.albab.mycollection.view.photocard.list.PhotocardListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class PhotocardViewModel @Inject constructor(
    private val photocardRepository: PhotocardRepository
) : ViewModel() {

    var photocardListUIState: StateFlow<PhotocardListUIState>? = null
    var photocardDetailsUIState: StateFlow<PhotocardDetailsUIState>? = null

    private val _fullImage = MutableStateFlow<Bitmap?>(null)
    val fullImage: StateFlow<Bitmap?> get() = _fullImage

    private val _cropRectangle = MutableStateFlow<Rectangle?>(null)
    val cropRectangle: StateFlow<Rectangle?> get() = _cropRectangle

    private val _rectanglesCropped = MutableStateFlow<ArrayList<Rectangle>>(arrayListOf())
    val rectanglesCropped: StateFlow<ArrayList<Rectangle>> get() = _rectanglesCropped

    private val _pcsSelected = MutableStateFlow<ArrayList<Photocard>>(arrayListOf())
    val pcsSelected: StateFlow<ArrayList<Photocard>> get() = _pcsSelected

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> get() = _showLoading

    fun getPhotocardByCollection(collectionId: String) {
        photocardListUIState = photocardRepository.getPhotocardsByCollection(collectionId)
            .map { items -> PhotocardListUIState.Success(items) }
            .catch { PhotocardListUIState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                PhotocardListUIState.Loading
            )
    }

    fun getPhotocardById(pcId: String) {
        photocardDetailsUIState = photocardRepository.getPhotocardById(pcId)
            .map { pc -> PhotocardDetailsUIState.Success(pc) }
            .catch { PhotocardDetailsUIState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                PhotocardDetailsUIState.Loading
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addPhotocard(title: String, description: String?, image: String, collectionId: Long) {
        viewModelScope.launch {
            photocardRepository.addPhotocard(
                Photocard(
                    title,
                    description,
                    image,
                    null,
                    LocalDateTime.now().toString(),
                    collectionId,
                )
            )
        }
    }

    fun updatePhotocard(photocard: Photocard) {
        viewModelScope.launch {
            photocardRepository.updatePhotocard(photocard)
        }
    }

    fun deletePhotocard(photocard: Photocard) {
        viewModelScope.launch {
            photocardRepository.deletePhotocard(photocard)
        }
    }

    fun onPCSelected(pcSelected: Boolean, photocard: Photocard) {
        if (pcSelected) {
            _pcsSelected.value.add(photocard)
        } else {
            _pcsSelected.value.remove(photocard)
        }
    }

    fun deleteSelectedPhotocards() {
        for (pc in _pcsSelected.value) {
            deletePhotocard(pc)
        }
        _pcsSelected.value.clear()
    }

    fun saveImage(image: Bitmap, width: Int, height: Int) {
        val baseWidth = image.width
        val baseHeight = image.height
        val scaleWidth = (width.toFloat())/baseWidth
        val scaleHeight = (height.toFloat())/baseHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(image, 0, 0, baseWidth, baseHeight, matrix, false)
        image.recycle()
        _fullImage.value = resizedBitmap
    }

    fun getRectangle(start: Offset, end: Offset) {
        _cropRectangle.value = Rectangle(start, end)
    }

    fun saveRectangle(rectangle: Rectangle) {
        _rectanglesCropped.value.add(rectangle)
        restartRectangle()
    }

    fun restartAllRectangles() {
        _cropRectangle.value = null
        _rectanglesCropped.value.clear()
    }

    fun restartRectangle() {
        _cropRectangle.value = null
    }

    fun clearLastRectangle() {
        restartRectangle()
        val copyList = _rectanglesCropped.value
        val last = copyList.last()
        copyList.remove(last)
        _rectanglesCropped.value = copyList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAndCropPhotocards(collectionId: String) {
        if (_fullImage.value != null) {
            _rectanglesCropped.value.forEach { rectangle ->
                val x: Float
                val y: Float
                if (rectangle.size.width<0 && rectangle.size.height<0) {
                    x = rectangle.end.x
                    y = rectangle.end.y
                } else if (rectangle.size.width>=0 && rectangle.size.height<0) {
                    x = rectangle.start.x
                    y = rectangle.end.y
                } else if (rectangle.size.width<0 && rectangle.size.height>=0) {
                    x = rectangle.end.x
                    y = rectangle.start.y
                } else {
                    x = rectangle.start.x
                    y = rectangle.start.y
                }
                val imageCropped =
                    Bitmap.createBitmap(
                        _fullImage.value!!,
                        x.toInt(),
                        y.toInt(),
                        abs(rectangle.size.width).toInt(),
                        abs(rectangle.size.height).toInt()
                    )
                addPhotocard("", null, ImageConverter.bitmapToBase64(imageCropped), collectionId.toLong())
            }
        }
        restartAllRectangles()
    }
}