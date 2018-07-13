## 弹窗选择拍照或从相册获取图片,结合Rxjava和Luban图片压缩的工具的封装,图片压缩效果最接近微信图片压缩。

## 显示弹窗获取图片的方法
### 方法一：不自动压缩图片的方法，及compressionFile的值可能为null
* PictureCapture.getPicture(context: Context, listener: OnPicturePathListener, needCrop: Boolean)
* 说明：参数2：图片回掉接口，返回Photo对象，带有（originalFile原始图片文件、compressionFile压缩后的图片文件、cropFile剪裁后的图片文件）
* 参数3：是否需要剪裁（若为false则cropFile的值为null）

### 方法二：自动压缩图片的方法，及compressionFile的值不为null
* PictureCapture.getCompressionPicture(context: Context, listener: OnPicturePathListener, needCrop: Boolean)

## 压缩图片可选的方法
### 使用Luban压缩工具封装后的方法
* PictureCompression.compressionPicture(context: Context, path: String, listener: OnPictureCompressionListener)
* PictureCompression.compressionPicture(context: Context, file: File, listener: OnPictureCompressionListener)
* PictureCompression.compressionPicture(context: Context, uri: Uri, listener: OnPictureCompressionListener)

### 使用Rxjava结合Luban压缩工具封装后的方法
* PictureCompression.compressionPictureRx(context: Context, path: String, listener: OnPictureCompressionListener)
* PictureCompression.compressionPictureRx(context: Context, file: File, listener: OnPictureCompressionListener)
* PictureCompression.compressionPictureRx(context: Context, uri: Uri, listener: OnPictureCompressionListener)
* PictureCompression.compressionPictureRx(context: Context, photo: Photo, listener: OnPicturePathListener)

* PictureCompression.compressionPictureRx(context: Context, photos: List<*>, listener: OnCompressionPicturesListener)
** 说明：（同时压缩多张图片的方法）此方法传入图片集合，集合泛型可以是 File、Uri、String类型，

### 比较：使用Rxjava结合Luban进行的图片压缩在时间上会比单独使用Luban进行压缩的时间快1-2毫秒

## 加入自定义通用提示弹窗：
### 使用方法：
* 创建对象并传入回掉接口，接口用于确定按钮的监听
    * TipsDialog(Context context,OnDropBtnClickListener listener)
* 使用对象调用弹窗的showDialog方法：
    * 方法一：showDialog(okBtnText: String?, vararg tipsText: String)
        * 说明：参数一：确定按钮文字；参数二：弹窗提示内容，可变参数
    * 方法二：showDialog(okBtnText: String?, type: Any, vararg tipsText: String)
        * 说明：参数二：type用于区分弹窗，在确定按钮的回掉接口中返回
    * 方法三：showDialog()

    * 设置文字颜色的方法：
     TipsDialog(this,object :TipsDialog.OnDropBtnClickListener{
                    override fun onClick(v: View, type: Any) {

                    }
                }).showDialog("OK","aaaaa","cccccc")
                        .setTextColor(Color.RED,Color.BLACK,Color.BLUE)

## 导入：
### 在app 的build.gradle中添加
    * implementation 'com.jushi:pictures_lib:1.0.4'

### 在项目的build.gradle中allprojects{repositories { }}里面添加
  * maven { url "https://raw.githubusercontent.com/JuShiDeveloper/PictureTool/master" }