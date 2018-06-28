# PictureTool
*弹窗选择拍照或从相册获取图片
*结合Rxjava和Luban图片压缩的工具的封装
*图片压缩效果最接近微信图片压缩。

##显示弹窗获取图片的方法
###方法一：不自动压缩图片的方法，及compressionFile的值可能为null
*PictureCapture.getPicture(context: Context, listener: OnPicturePathListener, needCrop: Boolean)
**说明：参数2：图片回掉接口，返回Photo对象，带有（originalFile原始图片文件、compressionFile压缩后的图片文件、cropFile剪裁后的图片文件）
**参数3：是否需要剪裁（若为false则cropFile的值为null）

###方法二：自动压缩图片的方法，及compressionFile的值不为null
*PictureCapture.getCompressionPicture(context: Context, listener: OnPicturePathListener, needCrop: Boolean)

