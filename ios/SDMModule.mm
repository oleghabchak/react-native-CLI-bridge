#import "SDMModule.h"
#import <React/RCTLog.h>
#import <SDM.h>
#import <SDM-Swift.h>

#define SDM_EVENT_STATE @"sdmEventDeviceState"
#define SDM_EVENT_DATA @"sdmEventNewData"

@implementation SDMModule
{
  bool hasListeners;
}

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
  return @[SDM_EVENT_STATE, SDM_EVENT_DATA];
}

RCT_EXPORT_METHOD(showDeviceManager)
{
  RCTLogInfo(@"*** DEVICE MANAGER");
  UIViewController* sdmVC = SerialioDeviceManager.shared.viewController;
  UIViewController* appVC = UIApplication.sharedApplication.keyWindow.rootViewController;
  if (appVC.presentedViewController == nil) {
    UINavigationController* navController = [[UINavigationController alloc] initWithRootViewController:sdmVC];
    navController.modalPresentationStyle = UIModalPresentationFormSheet;
    [appVC presentViewController:navController animated:YES completion:nil];
  }
}

RCT_EXPORT_METHOD(sendCommand:(NSString*)address command:(NSArray*)command)
{
  Device* device = [SerialioDeviceManager.shared connectedDeviceByName: address];
  if (device == nil) {
    RCTLogWarn(@"Device %@ not found", address);
    return;
  }
  NSMutableData* data = [NSMutableData dataWithCapacity:command.count];
  for (NSNumber* num in command) {
    if ([num isKindOfClass:[NSNumber class]] == NO) continue;
    UInt8 byte = num.intValue;
    [data appendBytes:&byte length:1];
  }
//  [device sendWithData:data complete:^(NSError* error){}];
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

- (instancetype)init
{
  self = [super init];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onDeviceConnecting:) name:SerialioDeviceManager.ConnectingToDevice object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onDeviceConnected:) name:SerialioDeviceManager.ConnectedToDevice object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onDeviceDisconnected:) name:SerialioDeviceManager.DisconnectedDevice object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onDeviceData:) name:SerialioDeviceManager.ReceivedScan object:nil];
    
    SerialioDeviceManager.shared.useNotifications = YES;
    SerialioDeviceManager.shared.connectToLastRecentDevicesEnabled = YES;
    //SerialioDeviceManager.shared.scanAndConnectToLastRecentDevicesEnabled = YES;
//    [SerialioDeviceManager.shared start];
  }
  return self;
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)onDeviceConnecting:(NSNotification*)notification
{
  Device* device = notification.userInfo[SerialioDeviceManager.UserInfoDeviceKey];
  RCTLogInfo(@"DEVICE connecting: %@", device.name);
  if (!hasListeners) return;
  [self sendEventWithName:SDM_EVENT_STATE body:@{@"address": device.name, @"state": @2}];
}

- (void)onDeviceConnected:(NSNotification*)notification
{
  Device* device = notification.userInfo[SerialioDeviceManager.UserInfoDeviceKey];
  RCTLogInfo(@"DEVICE connected: %@", device.name);
  if (!hasListeners) return;
  [self sendEventWithName:SDM_EVENT_STATE body:@{@"address": device.name, @"state": @3}];
}

- (void)onDeviceDisconnected:(NSNotification*)notification
{
  Device* device = notification.userInfo[SerialioDeviceManager.UserInfoDeviceKey];
  RCTLogInfo(@"DEVICE disconnected: %@", device.name);
  if (!hasListeners) return;
  [self sendEventWithName:SDM_EVENT_STATE body:@{@"address": device.name, @"state": @0}];
}

- (void)onDeviceData:(NSNotification*)notification
{
  Device* device = notification.userInfo[SerialioDeviceManager.UserInfoDeviceKey];
  Scan* scan = notification.userInfo[SerialioDeviceManager.UserInfoScanKey];
  RCTLogInfo(@"DEVICE: %@, DATA: %@", device.name, scan);
  if (!hasListeners) return;
  // use scan.data for CSN/EPC, scan.tid, scan.userdata or scan.ndef, [scan getText] return first non-nil NSString in this order
  [self sendEventWithName:SDM_EVENT_DATA body:@{@"address": device.name, @"data": [scan getText]}];  
}

@end
