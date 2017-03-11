//
//  ImageProcessing.m
//  ObjCIOSAppTest
//
//  Created by HSY on 2016. 12. 22..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import "ImageProcessing.h"

typedef enum {
    ALPHA = 0,
    BLUE = 1,
    GREEN = 2,
    RED = 3
}PIXELS;

@implementation ImageProcessing

- (void)AllocMemoryImage:(int)width Height:(int)height {
    imageWidth = width;
    imageHeight = height;
    
    rawImage = (uint8_t *) malloc(imageWidth * imageHeight * 4 * sizeof(char *));
    memset(rawImage, 0, imageWidth * imageHeight * 4 * sizeof(char *));
}

- (id)setImage:(UIImage *)image{
    [self DataInit];
    if (image == nil) {
        return nil;
    }
    
    CGSize size = image.size;
    
    [self AllocMemoryImage:size.width Height:size.height];
    
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    
    CGContextRef context = CGBitmapContextCreate(rawImage, imageWidth, imageHeight, 8, imageWidth * sizeof(uint32_t), colorSpace, kCGBitmapByteOrder32Little);
    
    CGContextDrawImage(context, CGRectMake(0, 0, imageWidth, imageHeight), image.CGImage);
    CGContextRelease(context);
    CGColorSpaceRelease( colorSpace );
    
    return self;
}

- (UIImage*)getImage{
    return [self BitmapToUIImage];
}

- (UIImage*)BitmapToUIImage:(unsigned char *) bitmap BitmapSize:(CGSize)size{
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef Context = CGBitmapContextCreate (rawImage, imageWidth, imageHeight, 8, imageWidth * 4, colorSpace, kCGBitmapByteOrder32Little | kCGImageAlphaPremultipliedLast);
    CGColorSpaceRelease(colorSpace );
    CGImageRef ref = CGBitmapContextCreateImage(Context);
    CGContextRelease(Context);
    UIImage *img = [UIImage imageWithCGImage:ref];
    CFRelease(ref);
    return img;
}

-(id)getGrayImage{
    for (int y=0; y<imageHeight; y++) {
        for (int x=0; x<imageWidth; x++) {
            uint8_t *pRawImage = (uint8_t *) &rawImage[(y * imageWidth + x) * 4];
            uint32_t gray = 0.299 * pRawImage[RED] + 0.587 * pRawImage[GREEN] + 0.114 * pRawImage[BLUE];
            
            pRawImage[RED] = (unsigned char) gray;
            pRawImage[GREEN] = (unsigned char) gray;
            pRawImage[BLUE] = (unsigned char) gray;
        }
    }
    return self;
}

-(id)getInverseImage{
    for(int y = 0; y < imageHeight; y++) {
        for(int x = 0; x < imageWidth; x++) {
            uint8_t *pRawImage = (uint8_t *) &rawImage[(y * imageWidth + x) * 4];
            
            pRawImage[RED] = 255 - pRawImage[RED];
            pRawImage[GREEN] = 255 - pRawImage[GREEN];
            pRawImage[BLUE] = 255 - pRawImage[BLUE];
        }
    }
    return self;
}

int getOffset(int x, int y, int width, int index) { return y * width * 4 + x * 4 + index;};

-(id)getTrackingImage{
    
    [self getGrayImage];
    
    int Matrix1[9] = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
    int Matrix2[9] = {-1, -2, -1, 0, 0, 0, 1, 2, 1};
    
    for (int y = 0; y < imageHeight; y++){
        for (int x = 0; x < imageWidth; x++){
            
            int sumr1 = 0, sumr2 = 0;
            int sumg1 = 0, sumg2 = 0;
            int sumb1 = 0, sumb2 = 0;
            
            int offset = 0;
            for (int j = 0; j <= 2; j++){
                for (int i = 0; i <= 2; i++){
                    sumr1 += *(rawImage + getOffset(x+i, y+j, imageWidth, RED)) * Matrix1[offset];
                    sumr2 += *(rawImage + getOffset(x+i, y+j, imageWidth, RED)) * Matrix2[offset];
                    
                    sumg1 += *(rawImage + getOffset(x+i, y+j, imageWidth, GREEN)) * Matrix1[offset];
                    sumg2 += *(rawImage + getOffset(x+i, y+j, imageWidth, GREEN)) * Matrix2[offset];
                    
                    sumb1 += *(rawImage + getOffset(x+i, y+j, imageWidth, BLUE)) * Matrix1[offset];
                    sumb2 += *(rawImage + getOffset(x+i, y+j, imageWidth, BLUE)) * Matrix2[offset];
                    
                    offset++;
                }
            }
            
            int sumr = MIN(((ABS(sumr1) + ABS(sumr2)) / 2), 255);
            int sumg = MIN(((ABS(sumg1) + ABS(sumg2)) / 2), 255);
            int sumb = MIN(((ABS(sumb1) + ABS(sumb2)) / 2), 255);
            
            uint8_t *pRawImage = (uint8_t *) &rawImage[(y * imageWidth + x) * 4];
            
            pRawImage[RED] = (unsigned char) sumr;
            pRawImage[GREEN] = (unsigned char) sumg;
            pRawImage[BLUE] = (unsigned char) sumb;
            pRawImage[ALPHA] = (unsigned char) rawImage[getOffset(x, y, imageWidth, ALPHA)];
        }
    }
    return self;
}

-(void)DataInit{
    if( rawImage ) {
        free(rawImage);
        rawImage = nil;
    }
}

@end































