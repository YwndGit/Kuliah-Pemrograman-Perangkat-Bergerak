import 'package:flutter/material.dart';

void main() {
  runApp(const StudentDashboardApp());
}

class StudentDashboardApp extends StatelessWidget {
  const StudentDashboardApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Student Dashboard',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF0866E8),
        ),
        scaffoldBackgroundColor: const Color(0xFFF8FAFF),
        fontFamily: 'Roboto',
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => const MainNavigationScreen(initialIndex: 0),
        '/courses': (context) => const MainNavigationScreen(initialIndex: 1),
        '/profile': (context) => const MainNavigationScreen(initialIndex: 2),
        '/counter_interacted': (context) => const MainNavigationScreen(
              initialIndex: 0,
              initialCounter: 5,
              initialFavorite: true,
            ),
        '/grades_detail': (context) => const MenuDetailScreen(
              title: 'Grades',
              icon: Icons.grade,
              accentColor: Color(0xFFE91E63),
            ),
        '/flutter_detail': (context) => const MenuDetailScreen(
              title: 'Flutter',
              icon: Icons.code,
              accentColor: Color(0xFF0879F9),
            ),
      },
    );
  }
}

/// Screen utama yang mengelola Bottom Navigation Bar (Home, Courses, Profile)
class MainNavigationScreen extends StatefulWidget {
  final int initialIndex;
  final int initialCounter;
  final bool initialFavorite;

  const MainNavigationScreen({
    super.key,
    this.initialIndex = 0,
    this.initialCounter = 0,
    this.initialFavorite = false,
  });

  @override
  State<MainNavigationScreen> createState() => _MainNavigationScreenState();
}

class _MainNavigationScreenState extends State<MainNavigationScreen> {
  late int _selectedIndex;

  // Halaman untuk masing-masing tab Bottom Navigation
  late final List<Widget> _pages;

  @override
  void initState() {
    super.initState();
    _selectedIndex = widget.initialIndex;
    _pages = [
      HomeDashboardView(
        initialCounter: widget.initialCounter,
        initialFavorite: widget.initialFavorite,
      ),
      const CoursesScreen(),
      const ProfileScreen(),
    ];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _pages[_selectedIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        onDestinationSelected: (index) {
          setState(() {
            _selectedIndex = index;
          });
        },
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.home_outlined),
            selectedIcon: Icon(Icons.home),
            label: 'Home',
          ),
          NavigationDestination(
            icon: Icon(Icons.menu_book_outlined),
            selectedIcon: Icon(Icons.menu_book),
            label: 'Courses',
          ),
          NavigationDestination(
            icon: Icon(Icons.person_outline),
            selectedIcon: Icon(Icons.person),
            label: 'Profile',
          ),
        ],
      ),
    );
  }
}

/// View untuk Tab Home (Student Dashboard utama)
class HomeDashboardView extends StatefulWidget {
  final int initialCounter;
  final bool initialFavorite;

  const HomeDashboardView({
    super.key,
    this.initialCounter = 0,
    this.initialFavorite = false,
  });

  @override
  State<HomeDashboardView> createState() => _HomeDashboardViewState();
}

class _HomeDashboardViewState extends State<HomeDashboardView> {
  late int counter;
  late bool isFavorite;

  @override
  void initState() {
    super.initState();
    counter = widget.initialCounter;
    isFavorite = widget.initialFavorite;
  }

  void incrementCounter() {
    setState(() {
      counter++;
    });
  }

  void decrementCounter() {
    setState(() {
      // Nilai minimum counter adalah 0 (tidak bisa negatif)
      if (counter > 0) {
        counter--;
      }
    });
  }

  void resetCounter() {
    setState(() {
      counter = 0;
    });
  }

  void toggleFavorite() {
    setState(() {
      isFavorite = !isFavorite;
    });

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(
          isFavorite ? 'Ditambahkan ke favorit!' : 'Dihapus dari favorit.',
        ),
        duration: const Duration(seconds: 1),
        behavior: SnackBarBehavior.floating,
      ),
    );
  }

  void _navigateToDetail(String title, IconData icon, Color color) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => MenuDetailScreen(
          title: title,
          icon: icon,
          accentColor: color,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF0866E8),
        foregroundColor: Colors.white,
        title: const Text(
          'Student Dashboard',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        actions: [
          IconButton(
            onPressed: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Tidak ada notifikasi baru.'),
                  duration: Duration(seconds: 1),
                ),
              );
            },
            icon: const Icon(Icons.notifications_none),
          ),
        ],
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // WELCOME SECTION
              const Text(
                'Hello, Student!',
                style: TextStyle(
                  fontSize: 30,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF101828),
                ),
              ),
              const SizedBox(height: 5),
              const Text(
                'Welcome to Mobile Programming',
                style: TextStyle(
                  fontSize: 17,
                  color: Colors.blueGrey,
                ),
              ),
              const SizedBox(height: 25),

              // STUDENT PROFILE CARD
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  gradient: const LinearGradient(
                    colors: [
                      Color(0xFF0879F9),
                      Color(0xFF0756D8),
                    ],
                  ),
                  borderRadius: BorderRadius.circular(24),
                  boxShadow: [
                    BoxShadow(
                      color: const Color(0xFF0866E8).withValues(alpha: 0.3),
                      blurRadius: 16,
                      offset: const Offset(0, 8),
                    ),
                  ],
                ),
                child: Row(
                  children: [
                    const CircleAvatar(
                      radius: 40,
                      backgroundColor: Colors.white,
                      child: CircleAvatar(
                        radius: 38,
                        backgroundColor: Color(0xFFE8F1FF),
                        child: Icon(
                          Icons.person,
                          size: 48,
                          color: Color(0xFF0866E8),
                        ),
                      ),
                    ),
                    const SizedBox(width: 15),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: const [
                          Text(
                            'Yuwand Arteta Hydri W.P',
                            style: TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                              color: Colors.white,
                            ),
                          ),
                          SizedBox(height: 4),
                          Text(
                            'NRP: 5053251020',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w500,
                              color: Colors.white70,
                            ),
                          ),
                          SizedBox(height: 2),
                          Text(
                            'Teknik Informatika / Mobile Dev',
                            style: TextStyle(
                              fontSize: 13,
                              color: Colors.white70,
                            ),
                          ),
                        ],
                      ),
                    ),
                    IconButton(
                      onPressed: toggleFavorite,
                      icon: Icon(
                        isFavorite ? Icons.favorite : Icons.favorite_border,
                        color: isFavorite ? Colors.redAccent : Colors.white,
                        size: 30,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 25),

              // INTERACTIVE COUNTER
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(22),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(24),
                  border: Border.all(
                    color: const Color(0xFFE0E7F2),
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.blue.withValues(alpha: 0.06),
                      blurRadius: 15,
                      offset: const Offset(0, 5),
                    ),
                  ],
                ),
                child: Column(
                  children: [
                    const Align(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        'Interactive Counter',
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    const SizedBox(height: 15),
                    Text(
                      '$counter',
                      style: const TextStyle(
                        fontSize: 55,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF0866E8),
                      ),
                    ),
                    const SizedBox(height: 15),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        _counterButton(
                          icon: Icons.remove,
                          onPressed: decrementCounter,
                        ),
                        _counterButton(
                          icon: Icons.add,
                          onPressed: incrementCounter,
                        ),
                        _counterButton(
                          icon: Icons.refresh,
                          onPressed: resetCounter,
                          backgroundColor: const Color(0xFFDCEEFF),
                          iconColor: const Color(0xFF0866E8),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 28),

              // LEARNING MENU
              const Text(
                'Learning Menu',
                style: TextStyle(
                  fontSize: 25,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF101828),
                ),
              ),
              const SizedBox(height: 18),
              GridView.count(
                crossAxisCount: 2,
                crossAxisSpacing: 15,
                mainAxisSpacing: 15,
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                childAspectRatio: 1.35,
                children: [
                  _menuCard(
                    icon: Icons.code,
                    title: 'Flutter',
                    color: const Color(0xFFDCEEFF),
                    iconColor: const Color(0xFF0879F9),
                    onTap: () => _navigateToDetail(
                      'Flutter',
                      Icons.code,
                      const Color(0xFF0879F9),
                    ),
                  ),
                  _menuCard(
                    icon: Icons.school,
                    title: 'Courses',
                    color: const Color(0xFFFFEFD9),
                    iconColor: Colors.orange,
                    onTap: () => _navigateToDetail(
                      'Courses',
                      Icons.school,
                      Colors.orange,
                    ),
                  ),
                  _menuCard(
                    icon: Icons.menu_book,
                    title: 'Materials',
                    color: const Color(0xFFDCF9DF),
                    iconColor: Colors.green,
                    onTap: () => _navigateToDetail(
                      'Materials',
                      Icons.menu_book,
                      Colors.green,
                    ),
                  ),
                  _menuCard(
                    icon: Icons.assignment,
                    title: 'Assignments',
                    color: const Color(0xFFEDE1FF),
                    iconColor: Colors.deepPurple,
                    onTap: () => _navigateToDetail(
                      'Assignments',
                      Icons.assignment,
                      Colors.deepPurple,
                    ),
                  ),
                  // Menu Baru Hasil Tugas Pengembangan: Grades / Nilai
                  _menuCard(
                    icon: Icons.grade,
                    title: 'Grades',
                    color: const Color(0xFFFFE5EB),
                    iconColor: const Color(0xFFE91E63),
                    onTap: () => _navigateToDetail(
                      'Grades',
                      Icons.grade,
                      const Color(0xFFE91E63),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _counterButton({
    required IconData icon,
    required VoidCallback onPressed,
    Color backgroundColor = const Color(0xFF0879F9),
    Color iconColor = Colors.white,
  }) {
    return SizedBox(
      width: 75,
      height: 55,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: backgroundColor,
          foregroundColor: iconColor,
          elevation: 0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(18),
          ),
        ),
        child: Icon(icon, size: 27),
      ),
    );
  }

  Widget _menuCard({
    required IconData icon,
    required String title,
    required Color color,
    required Color iconColor,
    required VoidCallback onTap,
  }) {
    return Card(
      elevation: 0,
      color: color,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(22),
      ),
      child: InkWell(
        borderRadius: BorderRadius.circular(22),
        onTap: onTap,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 42, color: iconColor),
            const SizedBox(height: 10),
            Text(
              title,
              textAlign: TextAlign.center,
              style: const TextStyle(
                fontSize: 17,
                fontWeight: FontWeight.bold,
                color: Color(0xFF101828),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// Halaman Detail Menu Terpisah (Tugas Pengembangan: Setiap menu menampilkan halaman berbeda)
class MenuDetailScreen extends StatelessWidget {
  final String title;
  final IconData icon;
  final Color accentColor;

  const MenuDetailScreen({
    super.key,
    required this.title,
    required this.icon,
    required this.accentColor,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(title),
        backgroundColor: accentColor,
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const SizedBox(height: 30),
            Center(
              child: Container(
                padding: const EdgeInsets.all(24),
                decoration: BoxDecoration(
                  color: accentColor.withValues(alpha: 0.15),
                  shape: BoxShape.circle,
                ),
                child: Icon(icon, size: 72, color: accentColor),
              ),
            ),
            const SizedBox(height: 20),
            Text(
              'Halaman $title',
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 10),
            Text(
              'Ini adalah tampilan terpisah untuk modul $title sesuai dengan instruksi tugas pengembangan.',
              textAlign: TextAlign.center,
              style: const TextStyle(fontSize: 15, color: Colors.blueGrey),
            ),
            const SizedBox(height: 30),
            Expanded(
              child: ListView(
                children: [
                  _buildDetailTile(
                    'Informasi Utama',
                    'Konten materi pembelajaran $title',
                    Icons.info_outline,
                  ),
                  _buildDetailTile(
                    'Aktivitas Terbaru',
                    'Daftar pembaruan modul dan tugas',
                    Icons.update,
                  ),
                  _buildDetailTile(
                    'Status Penyelesaian',
                    'Progres belajar Anda pada modul ini',
                    Icons.check_circle_outline,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDetailTile(String title, String subtitle, IconData leadingIcon) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
        side: const BorderSide(color: Color(0xFFE0E7F2)),
      ),
      child: ListTile(
        leading: Icon(leadingIcon, color: accentColor),
        title: Text(
          title,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        subtitle: Text(subtitle),
        trailing: const Icon(Icons.arrow_forward_ios, size: 16),
      ),
    );
  }
}

/// Halaman Courses untuk Tab Navigasi Bawah
class CoursesScreen extends StatelessWidget {
  const CoursesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final courses = [
      {'name': 'Pemrograman Perangkat Bergerak', 'code': 'IF2026', 'progress': 0.8},
      {'name': 'Desain Antarmuka Pengguna (UI/UX)', 'code': 'IF2025', 'progress': 0.95},
      {'name': 'Rekayasa Perangkat Lunak', 'code': 'IF2024', 'progress': 0.7},
      {'name': 'Pemrograman Berorientasi Objek', 'code': 'IF2023', 'progress': 1.0},
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Daftar Courses'),
        backgroundColor: const Color(0xFF0866E8),
        foregroundColor: Colors.white,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(20),
        itemCount: courses.length,
        itemBuilder: (context, index) {
          final c = courses[index];
          return Card(
            margin: const EdgeInsets.only(bottom: 16),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
              side: const BorderSide(color: Color(0xFFE0E7F2)),
            ),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(
                        child: Text(
                          c['name'] as String,
                          style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 8,
                          vertical: 4,
                        ),
                        decoration: BoxDecoration(
                          color: const Color(0xFFE8F1FF),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          c['code'] as String,
                          style: const TextStyle(
                            fontSize: 12,
                            color: Color(0xFF0866E8),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),
                  LinearProgressIndicator(
                    value: c['progress'] as double,
                    backgroundColor: const Color(0xFFE0E7F2),
                    color: const Color(0xFF0866E8),
                    minHeight: 6,
                    borderRadius: BorderRadius.circular(3),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Progres: ${((c['progress'] as double) * 100).toInt()}%',
                    style: const TextStyle(fontSize: 12, color: Colors.blueGrey),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}

/// Halaman Profile Mahasiswa untuk Tab Navigasi Bawah
class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Profil Mahasiswa'),
        backgroundColor: const Color(0xFF0866E8),
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            const Center(
              child: CircleAvatar(
                radius: 55,
                backgroundColor: Color(0xFF0866E8),
                child: CircleAvatar(
                  radius: 51,
                  backgroundColor: Colors.white,
                  child: Icon(
                    Icons.person,
                    size: 65,
                    color: Color(0xFF0866E8),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              'Yuwand Arteta Hydri W.P',
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
                color: Color(0xFF101828),
              ),
            ),
            const SizedBox(height: 4),
            const Text(
              'NRP: 5053251020',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.w500,
                color: Colors.blueGrey,
              ),
            ),
            const SizedBox(height: 24),
            const Divider(),
            const SizedBox(height: 12),
            _buildProfileItem(Icons.email_outlined, 'Email', 'ywndtik@gmail.com'),
            _buildProfileItem(Icons.school_outlined, 'Program Studi', 'Teknik Informatika'),
            _buildProfileItem(Icons.class_outlined, 'Mata Kuliah', 'Pemrograman Perangkat Bergerak'),
            _buildProfileItem(Icons.badge_outlined, 'Status', 'Mahasiswa Aktif'),
          ],
        ),
      ),
    );
  }

  Widget _buildProfileItem(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(icon, color: const Color(0xFF0866E8), size: 24),
          const SizedBox(width: 16),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                label,
                style: const TextStyle(fontSize: 12, color: Colors.blueGrey),
              ),
              const SizedBox(height: 2),
              Text(
                value,
                style: const TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.w600,
                  color: Color(0xFF101828),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
