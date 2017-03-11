//
//  MainViewController.h
//  AlarmIOSApp
//
//  Created by HSY on 2016. 12. 21..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainClockView.h"

@interface MainViewController : UIViewController {
    NSTimer *timer;
    IBOutlet UILabel *clockDisplay;
    IBOutlet MainClockView *pClockView;
}

- (void)onTimer;

@property Boolean pAlarmOnOff;
@property int pAlarmHour;
@property int pAlarmMinute;

@end
