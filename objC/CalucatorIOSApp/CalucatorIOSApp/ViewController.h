//
//  ViewController.h
//  CalucatorIOSApp
//
//  Created by HSY on 2016. 12. 20..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import <UIKit/UIKit.h>

// enum : 열거 데이터형
typedef enum {
    STATUS_DEFAULT = 0,     // C
    STATUS_DIVISION,        // /
    STATUS_MULTIPLY,        // *
    STATUS_MINUS,           // -
    STATUS_PLUS,            // +
    STATUS_RETURN           // =
} kStatusCode;

@interface ViewController : UIViewController{
    
    double curValue;            // 현재 입력값 저장 프로퍼티
    double totalCurValue;       // 최종 계산값 저장 프로퍼티
    NSString *curInputValue;    // 현재 입력된 문자열 저장 프로퍼티
    kStatusCode curStatusCode;  // 어떤 기호를 썼는지
}

// 숫자 버튼 클릭 이벤트 메서드
-(IBAction)digitPressed:(UIButton *)sender;
// 기능 버튼 클릭 이벤트 메서드
-(IBAction)operationPressed:(UIButton *)sender;

// 프로퍼티 
@property Float64 curValue;
@property Float64 totalCurValue;
@property kStatusCode curStatusCode;

@property (weak, nonatomic)IBOutlet UILabel *displayLabel;


@end

