import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:b21_printer/b21_printer.dart';

void main() {
  const MethodChannel channel = MethodChannel('b21_printer');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await B21Printer.info, '42');
  });
}
