//
//  ImgProcViewController.h
//  ObjCIOSAppTest
//
//  Created by HSY on 2016. 12. 22..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <UIKit/UIKit.h>
#import "ImageProcInfoViewController.h"
#import "ImageProcessing.h"

@interface ImgProcViewController : UIViewController <UIImagePickerControllerDelegate, UINavigationControllerDelegate>{
    ImageProcInfoViewController *pImageProcInfoViewController;
    IBOutlet UIButton *infoButton;
    IBOutlet UIImageView *pImageView;
    
    ImageProcessing *pImageProcessing;
    UIImage *orginImage;
}

- (IBAction)PushSetupClick;
- (IBAction)RunGeneralPicker;
- (IBAction)WhiteBlackImage;
- (IBAction)InverseImage;
- (IBAction)TrackingImage;

@property (strong, nonatomic) ImageProcInfoViewController *pImageProcInfoViewController;

@end
