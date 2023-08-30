
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNAwesomeModule2Spec.h"

@interface AwesomeModule2 : NSObject <NativeAwesomeModule2Spec>
#else
#import <React/RCTBridgeModule.h>

@interface AwesomeModule2 : NSObject <RCTBridgeModule>
#endif

@end
