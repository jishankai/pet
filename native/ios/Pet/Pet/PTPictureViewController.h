//
//  PTPictureViewController.h
//  Pet
//
//  Created by Ji Shankai on 14-5-19.
//  Copyright (c) 2014年 Ji Shankai. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PTPictureViewController : TMQuiltViewController
<UIImagePickerControllerDelegate, UINavigationControllerDelegate>

@property (nonatomic, retain) IBOutlet UIButton *takePictureButton;

- (IBAction)takePicture:(id)sender;

@end
