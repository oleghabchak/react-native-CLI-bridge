
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNAwesomeModule2Spec.h"

@interface AwesomeModule3 : NSObject <NativeAwesomeModule2Spec>
#else
#import <React/RCTBridgeModule.h>

@interface AwesomeModule3 : NSObject <RCTBridgeModule>
#endif

@end
