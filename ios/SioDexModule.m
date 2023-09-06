//
//  SioDexModule.m
//  DEX_DL_RN
//
//  Created by Serhii Martusenko on 22/06/2022.
//

#import "SioDexModule.h"
#import <React/RCTLog.h>
#import <siodex.h>
#import <sioDEX-Swift.h>
#import <SDM.h>
#import <SDM-Swift.h>

#define SIODEX_EVENT_TRACE @"sioDexEventTrace"
#define SIODEX_EVENT_DATA @"sioDexEventNewData"



@implementation SioDexModule
{
  bool hasListeners;
  __strong VendingMachine* vendingMachine;
  __block UIBackgroundTaskIdentifier backTaskId;
}

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
  return @[SIODEX_EVENT_TRACE, SIODEX_EVENT_DATA];
}


RCT_EXPORT_METHOD(startDownloading:(NSString*)deviceName  machineType:(NSNumber* _Nonnull)machineType)
{
  RCTLogInfo(@"*** Start DEX downloading");
  Device* device = [SerialioDeviceManager.shared connectedDeviceByName: deviceName];
  if (device == nil) {
    RCTLogWarn(@"Device %@ not found", deviceName);
    return;
  }
  
  if (![device isKindOfClass:[BlueSnapDB9Device class]]) {
    RCTLogWarn(@"Only BlueSnap M6A Device supported");
    return;
  }
  
  vendingMachine = [[VendingMachine alloc] initWithDevice:(BlueSnapDB9Device*)device trace:^(NSTimeInterval elapsed, NSString * _Nonnull msg) {
    if (!self->hasListeners) return;
    [self sendEventWithName:SIODEX_EVENT_TRACE body:@{@"address": device.name, @"message": msg, @"elapsed": @(elapsed)}];
  }];

  [vendingMachine startDownloadingWithMachineType: 0 complete:^(NSData * _Nullable data, NSError * _Nullable error) {
    if (error != nil) {
//      NSLog(@"Error :%@",[error userInfo])
      NSLog(@"Error :%@",error);
      
      [self sendEventWithName:SIODEX_EVENT_DATA body:@{@"address": device.name, @"error": error}];

      
    } else if (data != nil) {
   
        NSString *charlieSendString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"Data :%@",charlieSendString);
        [self sendEventWithName:SIODEX_EVENT_DATA body:@{@"address": device.name, @"data": charlieSendString}];
    }
  }];
}

RCT_EXPORT_METHOD(stopDownloading:(NSString*)deviceName  machineType:(NSNumber* _Nonnull)machineType)
{

  
  [vendingMachine cancelDownloading];

  
}






// Will be called when this module's first listener is added.
-(void)startObserving {
  hasListeners = YES;
  // Set up any upstream listeners or background tasks as necessary
}

// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
  hasListeners = NO;
  // Remove upstream listeners, stop unnecessary background tasks
}

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

@end
