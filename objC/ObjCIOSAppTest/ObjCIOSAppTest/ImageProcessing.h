//
//  ImageProcessing.h
//  ObjCIOSAppTest
//
//  Created by HSY on 2016. 12. 22..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ImageProcessing : NSObject {
    unsigned char*rawImage;
    
    int imageWidth;
    int imageHeight;
}

-(void)AllocMemoryImage:(int)width Height:(int)height;
-(id)setImage:(UIImage*)Image;
-(UIImage*)getImage;

-(UIImage*)BitmapToUIImage;
-(UIImage*)BitmapToUIImage:(unsigned char *) bitmap BitmapSize:(CGSize)size;
-(void)DataInit;

-(id)getGrayImage;
-(id)getInverseImage;
-(id)getTrackingImage;          

@end
