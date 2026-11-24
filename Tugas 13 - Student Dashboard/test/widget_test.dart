import 'package:flutter_test/flutter_test.dart';
import 'package:student_dashboard/main.dart';

void main() {
  testWidgets('Student Dashboard smoke test', (WidgetTester tester) async {
    await tester.pumpWidget(const StudentDashboardApp());
    expect(find.text('Student Dashboard'), findsWidgets);
    expect(find.text('Hello, Student!'), findsOneWidget);
  });
}
