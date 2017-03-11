//
//  ImgProcViewController.m
//  ObjCIOSAppTest
//
//  Created by HSY on 2016. 12. 22..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import "ImgProcViewController.h"

@implementation ImgProcViewController

- (void)viewDidLoad {
    pImageProcessing = [[ImageProcessing alloc] init];
    
    orginImage = [UIImage imageNamed:@"default.png"];
    [pImageView setImage:orginImage];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (BOOL)prefersStatusBarHidden { return YES; }

- (IBAction)PushSetupClick{
    if (pImageProcInfoViewController == nil){
        ImageProcInfoViewController *viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"ImageProcInfoViewController"];
        pImageProcInfoViewController = viewController;
    }
    
    [self presentViewController:pImageProcInfoViewController animated:YES completion:nil];
    
}

- (IBAction)RunGeneralPicker{
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    
    picker.delegate = self;
    picker.allowsEditing = NO;
    
    [self presentViewController:picker animated:YES completion:nil];
}

- (void)finishedPicker{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    orginImage = nil;
    orginImage =  [info objectForKey: UIImagePickerControllerOriginalImage];
    [self finishedPicker];
    
    [pImageView setImage:orginImage];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [self finishedPicker];
}

- (IBAction) WhiteBlackImage{
    pImageView.image = [[[pImageProcessing setImage:orginImage] getGrayImage] getImage];
}

- (IBAction) InverseImage{
    pImageView.image = [[[pImageProcessing setImage:orginImage] getInverseImage] getImage];
}

-(IBAction)TrackingImage{
    pImageView.image = [[[pImageProcessing setImage:orginImage] getTrackingImage] getImage];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
