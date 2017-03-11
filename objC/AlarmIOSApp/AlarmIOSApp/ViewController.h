//
//  ViewController.h
//  AlarmIOSApp
//
//  Created by HSY on 2016. 12. 21..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"
#import "SetupViewController.h"

@interface ViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIButton *infoButton;
@property (strong, nonatomic) MainViewController *mainViewController;
@property (strong, nonatomic) SetupViewController *setupViewController;

- (IBAction)setupClick;
- (IBAction)closeClick;
- (void) AlarmSetting;

@end

