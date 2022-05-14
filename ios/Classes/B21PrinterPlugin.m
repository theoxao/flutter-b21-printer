#import "B21PrinterPlugin.h"
#if __has_include(<b21_printer/b21_printer-Swift.h>)
#import <b21_printer/b21_printer-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "b21_printer-Swift.h"
#endif

@implementation B21PrinterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftB21PrinterPlugin registerWithRegistrar:registrar];
}
@end
