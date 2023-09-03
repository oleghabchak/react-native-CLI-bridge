#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

NS_ASSUME_NONNULL_BEGIN

@interface SDMModule : RCTEventEmitter<RCTBridgeModule>

- (void)showDeviceManager;
- (void)sendCommand:(NSString*)address command:(NSArray*)command;

@end

NS_ASSUME_NONNULL_END
