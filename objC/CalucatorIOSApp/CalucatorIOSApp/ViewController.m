//
//  ViewController.m
//  CalucatorIOSApp
//
//  Created by HSY on 2016. 12. 20..
//  Copyright © 2016년 HSY. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

@synthesize curValue;
@synthesize totalCurValue;
@synthesize curStatusCode;
@synthesize displayLabel;

-(void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    // 계산기 초기화 메서드 호출
    [self ClearCalculation];
    [super viewDidLoad];
}

// 메모리 오류시 메소드 호출
-(void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// 회전 여부 : YES
-(BOOL)shouldAutorotate { return YES; }
-(UIInterfaceOrientationMask) supportedInterfaceOrientations { return UIInterfaceOrientationMaskPortrait; }

// Label에 문자열 출력
-(void)DisplayInputValue:(NSString *)displayText{
    NSString *CommaText;
    CommaText = [self ConvertComma:displayText];
    [displayLabel setText:CommaText];
}

// 계산 결과 화면 출력
-(void)DisplayCalculationValue {
    NSString *displayText;
    displayText = [NSString stringWithFormat:@"%g",totalCurValue];
    [self DisplayInputValue:displayText];
    curInputValue = @"";
}

// 계산기 초기화
-(void) ClearCalculation{
    curInputValue = @"";
    curValue = 0;
    totalCurValue = 0;
    
    [self DisplayInputValue:curInputValue];
    
    curStatusCode = STATUS_DEFAULT;
}

// 천 원 단위 표시
-(NSString *)ConvertComma:(NSString *)data{
    if(data == nil) { return nil; }
    if([data length] <= 3) { return data; }
    
    NSString *integerString = nil;
    NSString *floatString = nil;
    NSString *minusSting = nil;
    
    // 소수점 찾기
    NSRange pointRange = [data rangeOfString:@"."];
    if(pointRange.location == NSNotFound){
        // 소수점 없을때
        integerString = data;
    } else {
        // 소수점 이하 영역 찾기
        NSRange r;
        r.location = pointRange.location;
        r.length = [data length] - pointRange.location;
        floatString = [data substringWithRange:r];
        
        // 정수 영역 찾기
        r.location = 0;
        r.length = pointRange.location;
        integerString = [data substringWithRange:r];
    }
    
    // 음수 부호 찾기
    NSRange minusRange = [integerString rangeOfString:@"-"];
    if(minusRange.location != NSNotFound) {
        // 음수 부호가 있을때
        minusSting = @"-";
        integerString = [integerString stringByReplacingOccurrencesOfString:@"-" withString:@""];
    }
    
    // 세자리 단위로 콤마 찍기
    NSMutableString *integerStringCommaInserted = [[NSMutableString alloc] init];
    NSUInteger integerStringLength = [integerString length];
    int idx = 0;
    while (idx < integerStringLength) {
        [integerStringCommaInserted appendFormat:@"%C",[integerString characterAtIndex:idx]];
        if((integerStringLength - (idx + 1)) %3 == 0 && integerStringLength != (idx + 1)){
            [integerStringCommaInserted appendString:@","];
        }
        idx++;
    }
    
    NSMutableString *returnString = [[NSMutableString alloc] init];
    if(minusSting != nil){ [returnString appendString:minusSting]; }
    if(floatString != nil) { [returnString appendString:floatString]; }
    return returnString;
}

// 숫자와 소수점 버튼 클릭시 호출
-(IBAction)digitPressed:(UIButton *)sender{
    NSString *numPoint = [[sender titleLabel] text];
    curInputValue = [curInputValue stringByAppendingFormat:@"%@", numPoint];
    [self DisplayInputValue:curInputValue];
}

-(IBAction)operationPressed:(UIButton *)sender{
    NSString *operationText = [[sender titleLabel] text];
    
    if([@"+"isEqualToString:operationText]){                        // 더하기 버튼 클릭시
        [self Calculation:curStatusCode CurStatusCode:STATUS_PLUS];
    } else if([@"-"isEqualToString:operationText]){                 // 빼기 버튼 클릭시
        [self Calculation:curStatusCode CurStatusCode:STATUS_MINUS];
    } else if ([@"*"isEqualToString:operationText]){                // 곱하기 버튼 클릭시
        [self Calculation:curStatusCode CurStatusCode:STATUS_MULTIPLY];
    } else if ([@"/"isEqualToString:operationText]){                // 나누기 버튼 클릭시
        [self Calculation:curStatusCode CurStatusCode:STATUS_DIVISION];
    } else if ([@"C"isEqualToString:operationText]){                // 초기화 버튼 클릭시
        [self ClearCalculation];
    } else if ([@"="isEqualToString:operationText]){                // 결과 버튼 클릭시
        [self Calculation:curStatusCode CurStatusCode:STATUS_RETURN];
    }
}

// 현재 상태에 해당도는 분기 처리 메서드
-(void)Calculation:(kStatusCode)StatusCode CurStatusCode:(kStatusCode)cStatusCode;{
    switch (StatusCode) {
        case STATUS_DEFAULT:
            [self DefaultCalculation];
            break;
        case STATUS_PLUS:
            [self PlusCalculation];
            break;
        case STATUS_MINUS:
            [self MinusCalculation];
            break;
        case STATUS_MULTIPLY:
            [self MultiplyCalculation];
            break;
        case STATUS_DIVISION:
            [self DivisionCalculation];
            break;
        default:
            break;
    }
    curStatusCode = cStatusCode;
}

// 초기화 이후 처음 입력된 값에 대한 처리
-(void)DefaultCalculation{
    curValue = [curInputValue doubleValue];
    totalCurValue = curValue;
    [self DisplayCalculationValue];
}

// 더하기 이후 처음 입력된 값에 대한 처리
-(void)PlusCalculation{
    curValue = [curInputValue doubleValue];
    totalCurValue = totalCurValue + curValue;
    [self DisplayCalculationValue];
}

// 빼기 이후 처음 입력된 값에 대한 처리
-(void)MinusCalculation{
    curValue = [curInputValue doubleValue];
    totalCurValue = totalCurValue - curValue;
    [self DisplayCalculationValue];
}

// 곱하기 이후 처음 입력된 값에 대한 처리
-(void)MultiplyCalculation{
    curValue = [curInputValue doubleValue];
    totalCurValue = totalCurValue * curValue;
    [self DisplayCalculationValue];
}

// 나누기 이후 처음 입력된 값에 대한 처리
-(void)DivisionCalculation{
    curValue = [curInputValue doubleValue];
    totalCurValue = totalCurValue / curValue;
    [self DisplayCalculationValue];
}
@end
