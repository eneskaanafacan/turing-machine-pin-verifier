# Turing Makinesi PIN Kontrolü

## 🏧 Proje Hakkında

Bu proje, ATM sistemlerinde kullanılan PIN doğrulama işlemini Turing makinesi teorisi ile modelleyen eğitim amaçlı bir simülasyon yazılımıdır. Kullanıcıdan alınan 4 haneli PIN kodunun, sistemde önceden tanımlanmış PIN koduyla karakter karakter eşleşip eşleşmediğini kontrol eder.

## 🚀 Hızlı Başlangıç

### 1. Derleyin
```bash
javac TuringMachinePIN.java
```

### 2. Çalıştırın
```bash
java TuringMachinePIN
```

### 3. PIN Girin
```
4 haneli PIN girin: 1234
```

## 📊 Test Senaryoları

### ✅ Başarılı Testler

#### Test 1: Tam Eşleşme
```
Girdi: 1234
Sistem PIN: 1234
Sonuç: ✅ Şifre doğru (ACCEPT)

Bant İlerleme:
#1234#1234# → #X234#Y234# → #XX34#YY34# → #XXX4#YYY4# → #XXXX#YYYY#
```

#### Test 2: Farklı Doğru PIN (Sistem PIN'i değiştirerek)
```
# Kodda systemPIN = "5678" yapın
Girdi: 5678
Sistem PIN: 5678
Sonuç: ✅ Şifre doğru (ACCEPT)
```

### ❌ Başarısız Testler

#### Test 3: Tamamen Farklı PIN
```
Girdi: 9876
Sistem PIN: 1234
Sonuç: ❌ Şifre hatalı (REJECT)
Sebep: İlk karakter (9≠1) eşleşmiyor
```

#### Test 4: Kısmi Eşleşme
```
Girdi: 1235
Sistem PIN: 1234
Sonuç: ❌ Şifre hatalı (REJECT)
Sebep: Son karakter (5≠4) eşleşmiyor
```

#### Test 5: Ters Sıralı
```
Girdi: 4321
Sistem PIN: 1234
Sonuç: ❌ Şifre hatalı (REJECT)
Sebep: İlk karakter (4≠1) eşleşmiyor
```

## 🔍 Detaylı Çalışma Örneği

### Senaryo: PIN 1234 vs Sistem 1234

**Başlangıç Bantı**: `#1234#1234#`

```
Adım 1:  [#]1234#1234#     Q0 → Q1 (# oku, # yaz, sağa git)
Adım 2:  #[1]234#1234#     Q1 → Q2 (1 oku, X yaz, 1'i hatırla, sağa git)
Adım 3:  #X[2]34#1234#     Q2 → Q2 (2 oku, 2 yaz, sağa git)
Adım 4:  #X2[3]4#1234#     Q2 → Q2 (3 oku, 3 yaz, sağa git)
Adım 5:  #X23[4]#1234#     Q2 → Q2 (4 oku, 4 yaz, sağa git)
Adım 6:  #X234[#]1234#     Q2 → Q3 (# oku, # yaz, sağa git)
Adım 7:  #X234#[1]234#     Q3 → Q4 (1 oku, Y yaz, hatırlanan 1 ile eşleşti, sola git)
Adım 8:  #X234[#]Y234#     Q4 → Q4 (# oku, # yaz, sola git)
...
(Banda başa dönüş)
Adım 15: [#]X234#Y234#     Q4 → Q5 (B oku, B yaz, sağa git)
Adım 16: #[X]234#Y234#     Q5 → Q1 (X oku, X yaz, dur - sıradaki işlenmemiş)
...
(İkinci karakter 2 için işlem)
Sonuç: Tüm karakterler eşleştikten sonra ACCEPT
```

## ⚙️ Sistem PIN Değiştirme

```java
// TuringMachinePIN.java içinde 141. satır civarı
final String systemPIN = "1234"; // Bu değeri değiştirin

// Örnek değişiklikler:
final String systemPIN = "0000"; // Test PIN
final String systemPIN = "9999"; // Zor PIN
final String systemPIN = "1357"; // Farklı PIN
```

## 🔧 Özelleştirme Seçenekleri

### 1. Dosyadan PIN Okuma
```java
// Sistem PIN'ini dosyadan oku
try {
    systemPIN = Files.readString(Paths.get("pin.txt")).trim();
} catch (IOException e) {
    systemPIN = "1234"; // Varsayılan
}
```