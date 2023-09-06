//
//  SioDexModule.h
//  DEX_DL_RN
//
//  Created by Serhii Martusenko on 22/06/2022.
//

#import <UIKit/UIKit.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>



NS_ASSUME_NONNULL_BEGIN

@interface SioDexModule : RCTEventEmitter<RCTBridgeModule>


- (void)startDownloading:(NSString*)deviceName machineType:(NSNumber* _Nonnull)machineType;

@end

NS_ASSUME_NONNULL_END
