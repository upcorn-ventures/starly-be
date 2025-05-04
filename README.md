# 📣 Starly Notification Backend

Java + Spring Boot ile geliştirilen bu servis, mobil cihazlara Firebase (Android) ve APNs (iOS) üzerinden push bildirimleri göndermek amacıyla tasarlanmıştır. Cihaz token kaydı, kullanıcı bildirim tercihleri ve zamanlanmış bildirimler gibi temel işlevleri destekler.

---

## 🚀 Özellikler

- Cihaz token kaydı (FCM + APNs)
- Tekil cihaza bildirim gönderimi
- Broadcast (tüm cihazlara) bildirim gönderimi
- Kullanıcı bildirim tercihleri (zaman dilimi, saat)
- Arka planda çalışan cron job ile zamanlanmış bildirimler

---

## 🧱 API Uç Noktaları

### `POST /device-token/register`

📌 Cihaz bilgilerini kaydeder (FCM + APNs token'ları dahil).

#### Örnek İstek:
``json
{
  "deviceId": "23044A45-6B4D-40EF-8AA0-6AE68F1A4328",
  "fcmToken": "fcm-token",
  "apnsToken": "apns-token",
  "platform": "ios"
}

### `POST /device-token/register`

📌 Cihaz bilgilerini kaydeder (FCM + APNs token'ları dahil).

#### Örnek İstek:
json
{
  "deviceId": "23044A45-6B4D-40EF-8AA0-6AE68F1A4328",
  "fcmToken": "fcm-token",
  "apnsToken": "apns-token",
  "platform": "ios"
}
