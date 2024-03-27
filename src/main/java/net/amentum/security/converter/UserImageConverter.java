package net.amentum.security.converter;

import net.amentum.security.model.UserImage;
import net.amentum.security.views.UserImageView;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class UserImageConverter{

    private final Logger logger = LoggerFactory.getLogger(UserImageConverter.class);

    public UserImage fromView(UserImageView view){
        UserImage image = new UserImage();
        image.setContentType(view.getContentType());
        image.setCreatedDate(view.getCreatedDate());
        try {
            if(image.getContentType().contains("image/")){
                if(view.getEncodedImageContentSmall() != null && !view.getEncodedImageContentSmall().isEmpty()){
                    image.setImageContentSmall(Base64.decodeBase64(view.getEncodedImageContentSmall()));
                }
                image.setImageContent(Base64.decodeBase64(view.getEncodedImageContent()));
                double bytes =  image.getImageContent().length;
                double kilobytes = (bytes / 1024);
                double megabytes = (kilobytes / 1024);
                if(megabytes > 2){
                    BufferedImage bufferedImage = null;
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();

                    bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getImageContent()));

                    ImageWriteParam param = writer.getDefaultWriteParam();
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(0.2f);

                    writer.setOutput(ImageIO.createImageOutputStream(os));
                    writer.write(null, new IIOImage(bufferedImage, null, null), param);
                    writer.dispose();

                    image.setImageContent(os.toByteArray());
                }else{
                    image.setImageContent(Base64.decodeBase64(view.getEncodedImageContent()));
                }

            }

        }catch (Exception ex){}
        image.setImageName(view.getImageName());
        image.setUserImageId(view.getUserImageId());
        return image;
    }

    public UserImageView fromEntity(UserImage entity, Boolean small){
        UserImageView view = new UserImageView();
        view.setContentType(entity.getContentType());
        view.setCreatedDate(entity.getCreatedDate());
        try{
            if(small != null && small){
                if(entity.getImageContentSmall() != null ){
                    view.setEncodedImageContentSmall(Base64.encodeBase64String(entity.getImageContentSmall()));
                }
            }else if(small != null && !small){
                view.setEncodedImageContent(Base64.encodeBase64String(entity.getImageContent()));
            }else if(small == null){
                if(entity.getImageContentSmall() != null ){
                    view.setEncodedImageContentSmall(Base64.encodeBase64String(entity.getImageContentSmall()));
                    view.setEncodedImageContent(Base64.encodeBase64String(entity.getImageContent()));
                }
            }


        }catch (Exception ex){
        }
        view.setImageName(entity.getImageName());
        view.setUserImageId(entity.getUserImageId());
        return view;
    }



}
