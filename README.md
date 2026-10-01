# PPC Auction Simulator

Hệ thống mô phỏng đấu giá từ khóa PPC/SEM phục vụ học tập. Ứng dụng minh họa mối quan hệ giữa bid, Quality Score, Ad Rank, vị trí, CPC, click và ngân sách. Đây không phải nền tảng quảng cáo thật và không kết nối Google Ads.

## Công nghệ

- Java JDK 21, Spring Boot 4, Maven
- Spring MVC, Thymeleaf, Bootstrap 5, CSS và JavaScript
- Spring Data JPA / Hibernate, SQL Server 2025 Express
- Microsoft JDBC Driver `13.6.0.jre11`
- Chart.js cho biểu đồ

## Yêu cầu môi trường

- JDK 21
- SQL Server 2025 Express, instance `localhost\SQLEXPRESS01`
- Database có sẵn tên `ppc_auction_db` và SQL login `ppcapp`
- Maven Wrapper đi kèm project

Ứng dụng không tạo hoặc xóa database. Hibernate dùng `ddl-auto=update` để bổ sung các bảng/cột còn thiếu cho ứng dụng, không dùng `create` hoặc `create-drop`.

## Cấu hình database

Cấu hình mặc định trong `src/main/resources/application.properties` dùng JDBC `instanceName=SQLEXPRESS01`, database `ppc_auction_db` và bật mã hóa kết nối với chứng chỉ máy chủ được tin cậy cho môi trường phát triển cục bộ.

Đặt các biến môi trường trước khi chạy:

```text
DB_USERNAME=ppcapp
DB_PASSWORD=<mật khẩu SQL Server của bạn>
```

`DB_URL` là tùy chọn; nếu cần URL riêng, đặt biến này. Nếu không, ứng dụng dùng URL theo instance mặc định trong `application.properties`. Không lưu mật khẩu vào source code hoặc README.

### IntelliJ IDEA

1. Chọn Project SDK và Run Configuration dùng JDK 21.
2. Mở **Run → Edit Configurations…**, chọn cấu hình ứng dụng Spring Boot.
3. Điền `DB_USERNAME` và `DB_PASSWORD` trong **Environment variables**.
4. Chạy `PpcAuctionApplication`.

## Chức năng

- CRUD nhà quảng cáo, từ khóa và chiến dịch; tìm kiếm/lọc và validation tiếng Việt.
- Mô phỏng phiên đấu giá từ khóa, lưu lịch sử và xem chi tiết phiên.
- Mô phỏng chiến dịch theo 1, 7 hoặc 30 ngày bằng dữ liệu chiến dịch đã cấu hình.
- Dashboard gồm 8 KPI và 5 biểu đồ dựa trên dữ liệu đã lưu.
- API đọc dữ liệu qua DTO tại `/api/advertisers`, `/api/keywords`, `/api/campaigns`, `/api/auctions` và `/api/dashboard`.
- Dữ liệu demo được thêm từng nhóm khi nhóm tương ứng chưa có dữ liệu; không tạo bản sao ở lần khởi động tiếp theo.

## Công thức mô phỏng

- `Ad Rank = Bid × Quality Score`
- Xếp hạng Ad Rank giảm dần; vị trí bắt đầu từ 1.
- Với vị trí không cuối: `CPC = min(Bid, Ad Rank kế tiếp / Quality Score hiện tại + 1)`.
- Với vị trí cuối: `CPC = Bid`.
- `Clicks = làm tròn(Impressions × CTR / 100)`.
- `Cost = Clicks × CPC`.
- `Ngân sách còn lại = max(0, Budget − Cost)`.

Đây là công thức giản lược để giải thích trong đồ án, không mô tả chính xác thuật toán của Google Ads hay hệ thống quảng cáo khác.

## Cấu trúc chính

```text
src/main/java/org/example/ppcauction/
  controller/  MVC controllers và REST API
  dto/         form và response DTO
  entity/      Advertiser, Keyword, Campaign, CampaignKeyword, AuctionResult
  repository/  Spring Data JPA repositories
  service/     CRUD, đấu giá, mô phỏng chiến dịch, dashboard
  config/      khởi tạo dữ liệu demo an toàn
src/main/resources/
  templates/   giao diện Thymeleaf tiếng Việt
  static/      CSS và JavaScript
```

## Chạy và kiểm thử

```powershell
.\mvnw.cmd clean verify
```

Chạy ứng dụng bằng cấu hình Spring Boot trong IntelliJ hoặc:

```powershell
.\mvnw.cmd spring-boot:run
```

## Trang chính

- `/` — Trang chủ
- `/dashboard` — Dashboard
- `/auction` — Mô phỏng phiên đấu giá
- `/advertisers`, `/keywords`, `/campaigns` — Quản lý dữ liệu
- `/campaign-simulator` — Mô phỏng nhiều ngày
- `/history` — Lịch sử

Ứng dụng là mô hình học tập cục bộ, không có đăng nhập, API quảng cáo bên ngoài hay thanh toán.
