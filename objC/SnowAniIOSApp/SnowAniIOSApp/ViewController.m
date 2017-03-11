//
//  ViewController.m
//  SnowAniIOSApp
//
//  Created by HSY on 2016. 12. 21..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import "ViewController.h"
#import "SnowAniViewController.h"

@interface ViewController ()

@end

@implementation ViewController

@synthesize infoButton;

- (void)viewDidLoad {
    
    SnowAniViewController *viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"SnowAniViewController"];
    
    // 기본 화면 위에 스노우 화면 위에 버튼
    [self.view insertSubview:viewController.view belowSubview:infoButton];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

// 상태바 숨김 여부 설정 메서드
- (BOOL)prefersStatusBarHidden{ return YES; }

/* 색깔 바꿀때 사용
- (UIStatusBarStyle)preferredStatusBarStyle{ return UIStatusBarStyleLightContent; }
*/


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
