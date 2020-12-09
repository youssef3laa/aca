package com.asset.appwork.otds.enums;

import java.util.Arrays;

/**
 * Created by karim on 10/22/20.
 */
public enum ContentType {
    MIME_IMAGE_BMP("image/bmp", "bmp"),
    MIME_IMAGE_CGM("image/cgm","cgm"),
    MIME_IMAGE_GIF("image/gif","gif"),
    MIME_IMAGE_IEF("image/ief","ief"),
    MIME_IMAGE_JPEG("image/jpeg","jpeg"),
    MIME_IMAGE_JPG("image/jpg","jpg"),
    MIME_IMAGE_JPE("image/jpe","jpe"),
    MIME_IMAGE_TIFF("image/tiff", "tiff"),
    MIME_IMAGE_TIF("image/tif", "tif"),
    MIME_IMAGE_PNG("image/png","png"),
    MIME_IMAGE_SVG_XML("image/svg+xml","svg"),
    MIME_IMAGE_VND_DJV("image/vnd.djv","djv"),
    MIME_IMAGE_VND_DJVU("image/vnd.djvu","djvu"),
    MIME_IMAGE_WAP_WBMP("image/vnd.wap.wbmp","wbmp"),
    MIME_IMAGE_X_CMU_RASTER("image/x-cmu-raster","ras"),
    MIME_IMAGE_X_ICON("image/x-icon","ico"),
    MIME_IMAGE_X_PORTABLE_ANYMAP("image/x-portable-anymap","pnm"),
    MIME_IMAGE_X_PORTABLE_BITMAP("image/x-portable-bitmap","pbm"),
    MIME_IMAGE_X_PORTABLE_GRAYMAP("image/x-portable-graymap","pgm"),
    MIME_IMAGE_X_PORTABLE_PIXMAP("image/x-portable-pixmap","ppm"),
    MIME_IMAGE_X_RGB("image/x-rgb","rgb");

    private final String contentType;
    private final String extension;

    private ContentType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static ContentType findExtension(String contentType) {
        return Arrays.stream(ContentType.values())
                .filter(type -> contentType.equalsIgnoreCase(type.getContentType()))
                .findFirst()
                .orElse( null);
    }

    public static ContentType findContentType(String ext) {
        return Arrays.stream(ContentType.values())
                .filter(alphabet -> ext.equalsIgnoreCase(alphabet.getExtension()))
                .findFirst()
                .orElse( null);
    }

}
