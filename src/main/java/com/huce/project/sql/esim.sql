DROP database project_esim ;
CREATE DATABASE IF NOT EXISTS project_esim;

USE project_esim;
-- Bảng user
CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN') DEFAULT 'ADMIN',
    created_at DATETIME NOT NULL
);

-- Bảng khu vực
CREATE TABLE areas (
    area_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    area_name VARCHAR(100) NOT NULL
);

-- Bảng quốc gia
CREATE TABLE countries (
    country_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    country_code CHAR(2) UNIQUE NOT NULL,
    country_name VARCHAR(100) NOT NULL,
    country_image VARCHAR(200) NOT NULL,
    area_id INT UNSIGNED,
    FOREIGN KEY (area_id) REFERENCES areas(area_id)
);

-- Bảng phiếu giảm giá
CREATE TABLE coupons (
    coupon_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    coupon_code VARCHAR(50) UNIQUE NOT NULL, 
    discount_value DECIMAL(10, 2) NOT NULL,        -- Giá trị giảm giá (có thể là số tiền hoặc phần trăm)
    discount_type ENUM('PERCENTAGE', 'AMOUNT') NOT NULL,  -- Chỉnh sửa ENUM thành viết hoa
    expiration_date DATE,                          
    is_active BOOLEAN DEFAULT TRUE,                
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng eSIM
CREATE TABLE esims (
    esim_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    esim_name VARCHAR(100) NOT NULL,
    esim_tag BOOlEAN,
    country_id INT unsigned,
    hotspot BOOLEAN,                  
    policy VARCHAR(255),              
    expiration VARCHAR(50),           
    voice_calls_sms BOOLEAN,          
    verified BOOLEAN,                 
    description TEXT,
    provider VARCHAR(100),
    esim_image VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(country_id) REFERENCES countries(country_id)
);

-- Bảng tùy chọn sản phẩm
CREATE TABLE options (
    option_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    duration INT UNSIGNED NOT NULL,   
    data INT UNSIGNED NOT NULL,
    unit VARCHAR(50),
    is_daily_plan BOOLEAN,            
    description TEXT, 
    price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng liên kết esims và options
CREATE TABLE esim_options (
    esim_id INT UNSIGNED,
    option_id INT UNSIGNED,
    option_amount INT UNSIGNED,       
    option_sale int UNSIGNED DEFAULT 0,
    PRIMARY KEY (esim_id, option_id),
    FOREIGN KEY (esim_id) REFERENCES esims(esim_id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES options(option_id) ON DELETE CASCADE
);
CREATE TABLE orders (
    order_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status ENUM("PENDING", "COMPLETED"),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20)
);
CREATE TABLE order_items (
    order_item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id INT UNSIGNED,
    esim_id INT UNSIGNED,
    option_id INT UNSIGNED,
    quantity INT UNSIGNED NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (esim_id) REFERENCES esims(esim_id),
    FOREIGN KEY (esim_id, option_id) REFERENCES esim_options(esim_id, option_id)
);

-- Bảng thanh toán
CREATE TABLE payments (
    payment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id INT UNSIGNED,
    payment_amount DECIMAL(10, 2) NOT NULL,
    currency_code CHAR(3) NOT NULL DEFAULT 'USD',
    payment_method VARCHAR(50) NOT NULL DEFAULT 'PayPal',
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED' ) DEFAULT 'PENDING',
    transaction_id VARCHAR(100),
    paypal_payer_id VARCHAR(100),
    paypal_payer_email VARCHAR(255),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_details JSON,  
    expiration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE SET NULL
);



-- Bảng blog
CREATE TABLE blogs (
    blog_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    blog_image VARCHAR(300),
    sumary TEXT,
    title TEXT NOT NULL,
    content LONGTEXT NOT NULL,
    author ENUM("ADMIN") DEFAULT "ADMIN",
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng câu hỏi thường gặp
CREATE TABLE faqs (
    faq_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL
);

-- Bảng thông tin về chúng tôi
CREATE TABLE about_us (
    about_us_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    description VARCHAR(400),
    image VARCHAR(400)
);

-- Bảng đối tác
CREATE TABLE partner_hub (
    partner_hub_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    description VARCHAR(400),
    image VARCHAR(400)
);



-- Chèn dữ liệu cho bảng khu vực
INSERT INTO areas (area_name) VALUES
('Asia'),
('Europe'),
('Africa'),
('North America'),
('South America'),
('Oceania'),
('Antarctica');

-- Chèn dữ liệu cho bảng quốc gia
-- Châu Á
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('kh', 'Cambodia', 1, 'https://flagcdn.com/kh.svg'),
('cn', 'China', 1, 'https://flagcdn.com/cn.svg'),
('in', 'India', 1, 'https://flagcdn.com/in.svg'),
('id', 'Indonesia', 1, 'https://flagcdn.com/id.svg'),
('ir', 'Iran', 1, 'https://flagcdn.com/ir.svg'),
('iq', 'Iraq', 1, 'https://flagcdn.com/iq.svg'),
('il', 'Israel', 1, 'https://flagcdn.com/il.svg'),
('jp', 'Japan', 1, 'https://flagcdn.com/jp.svg'),
('jo', 'Jordan', 1, 'https://flagcdn.com/jo.svg'),
('kz', 'Kazakhstan', 1, 'https://flagcdn.com/kz.svg'),
('kw', 'Kuwait', 1, 'https://flagcdn.com/kw.svg'),
('kg', 'Kyrgyzstan', 1, 'https://flagcdn.com/kg.svg'),
('la', 'Laos', 1, 'https://flagcdn.com/la.svg'),
('lb', 'Lebanon', 1, 'https://flagcdn.com/lb.svg'),
('my', 'Malaysia', 1, 'https://flagcdn.com/my.svg'),
('mv', 'Maldives', 1, 'https://flagcdn.com/mv.svg'),
('mn', 'Mongolia', 1, 'https://flagcdn.com/mn.svg'),
('mm', 'Myanmar', 1, 'https://flagcdn.com/mm.svg'),
('np', 'Nepal', 1, 'https://flagcdn.com/np.svg'),
('kp', 'North Korea', 1, 'https://flagcdn.com/kp.svg'),
('om', 'Oman', 1, 'https://flagcdn.com/om.svg'),
('pk', 'Pakistan', 1, 'https://flagcdn.com/pk.svg'),
('ps', 'Palestine', 1, 'https://flagcdn.com/ps.svg'),
('ph', 'Philippines', 1, 'https://flagcdn.com/ph.svg'),
('qa', 'Qatar', 1, 'https://flagcdn.com/qa.svg'),
('sa', 'Saudi Arabia', 1, 'https://flagcdn.com/sa.svg'),
('sg', 'Singapore', 1, 'https://flagcdn.com/sg.svg'),
('kr', 'South Korea', 1, 'https://flagcdn.com/kr.svg'),
('lk', 'Sri Lanka', 1, 'https://flagcdn.com/lk.svg'),
('sy', 'Syria', 1, 'https://flagcdn.com/sy.svg'),
('tw', 'Taiwan', 1, 'https://flagcdn.com/tw.svg'),
('th', 'Thailand', 1, 'https://flagcdn.com/th.svg'),
('tl', 'Timor-Leste', 1, 'https://flagcdn.com/tl.svg'),
('tr', 'Turkey', 1, 'https://flagcdn.com/tr.svg'),
('uz', 'Uzbekistan', 1, 'https://flagcdn.com/uz.svg'),
('vn', 'Vietnam', 1, 'https://flagcdn.com/vn.svg'),
('ye', 'Yemen', 1, 'https://flagcdn.com/ye.svg');

-- Châu Âu
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('ad', 'Andorra', 2, 'https://flagcdn.com/ad.svg'),
('al', 'Albania', 2, 'https://flagcdn.com/al.svg'),
('am', 'Armenia', 2, 'https://flagcdn.com/am.svg'),
('at', 'Austria', 2, 'https://flagcdn.com/at.svg'),
('ax', 'Åland Islands', 2, 'https://flagcdn.com/ax.svg'),
('ba', 'Bosnia and Herzegovina', 2, 'https://flagcdn.com/ba.svg'),
('be', 'Belgium', 2, 'https://flagcdn.com/be.svg'),
('bg', 'Bulgaria', 2, 'https://flagcdn.com/bg.svg'),
('by', 'Belarus', 2, 'https://flagcdn.com/by.svg'),
('ch', 'Switzerland', 2, 'https://flagcdn.com/ch.svg'),
('cy', 'Cyprus', 2, 'https://flagcdn.com/cy.svg'),
('cz', 'Czechia', 2, 'https://flagcdn.com/cz.svg'),
('de', 'Germany', 2, 'https://flagcdn.com/de.svg'),
('dk', 'Denmark', 2, 'https://flagcdn.com/dk.svg'),
('fr', 'France', 2, 'https://flagcdn.com/fr.svg'),
('gb', 'United Kingdom', 2, 'https://flagcdn.com/gb.svg'),
('nl', 'Netherlands', 2, 'https://flagcdn.com/nl.svg'),
('no', 'Norway', 2, 'https://flagcdn.com/no.svg'),
('pl', 'Poland', 2, 'https://flagcdn.com/pl.svg'),
('pt', 'Portugal', 2, 'https://flagcdn.com/pt.svg'),
('ua', 'Ukraine', 2, 'https://flagcdn.com/ua.svg'),
('va', 'Vatican City (Holy See)', 2, 'https://flagcdn.com/va.svg');

-- Châu Phi
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('ao', 'Angola', 3, 'https://flagcdn.com/ao.svg'),
('bf', 'Burkina Faso', 3, 'https://flagcdn.com/bf.svg'),
('bi', 'Burundi', 3, 'https://flagcdn.com/bi.svg'),
('bj', 'Benin', 3, 'https://flagcdn.com/bj.svg'),
('bw', 'Botswana', 3, 'https://flagcdn.com/bw.svg'),
('dz', 'Algeria', 3, 'https://flagcdn.com/dz.svg'),
('eg', 'Egypt', 3, 'https://flagcdn.com/eg.svg'),
('gq', 'Equatorial Guinea', 3, 'https://flagcdn.com/gq.svg'),
('gw', 'Guinea-Bissau', 3, 'https://flagcdn.com/gw.svg'),
('ke', 'Kenya', 3, 'https://flagcdn.com/ke.svg'),
('km', 'Comoros', 3, 'https://flagcdn.com/km.svg'),
('lr', 'Liberia', 3, 'https://flagcdn.com/lr.svg'),
('ls', 'Lesotho', 3, 'https://flagcdn.com/ls.svg'),
('ly', 'Libya', 3, 'https://flagcdn.com/ly.svg'),
('ma', 'Morocco', 3, 'https://flagcdn.com/ma.svg'),
('mg', 'Madagascar', 3, 'https://flagcdn.com/mg.svg');


-- Bắc Mỹ
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('bm', 'Bermuda', 4, 'https://flagcdn.com/bm.svg'),
('ca', 'Canada', 4, 'https://flagcdn.com/ca.svg'),
('cu', 'Cuba', 4, 'https://flagcdn.com/cu.svg'),
('do', 'Dominican Republic', 4, 'https://flagcdn.com/do.svg'),
('gl', 'Greenland', 4, 'https://flagcdn.com/gl.svg'),
('mx', 'Mexico', 4, 'https://flagcdn.com/mx.svg'),
('us', 'United States', 4, 'https://flagcdn.com/us.svg');

-- Nam Mỹ
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('ar', 'Argentina', 5, 'https://flagcdn.com/ar.svg'),
('bo', 'Bolivia', 5, 'https://flagcdn.com/bo.svg'),
('br', 'Brazil', 5, 'https://flagcdn.com/br.svg'),
('cl', 'Chile', 5, 'https://flagcdn.com/cl.svg'),
('co', 'Colombia', 5, 'https://flagcdn.com/co.svg'),
('ec', 'Ecuador', 5, 'https://flagcdn.com/ec.svg');

-- Châu Đại Dương
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('au', 'Australia', 6, 'https://flagcdn.com/au.svg'),
('fj', 'Fiji', 6, 'https://flagcdn.com/fj.svg'),
('fm', 'Micronesia', 6, 'https://flagcdn.com/fm.svg'),
('gu', 'Guam', 6, 'https://flagcdn.com/gu.svg'),
('ki', 'Kiribati', 6, 'https://flagcdn.com/ki.svg'),
('mh', 'Marshall Islands', 6, 'https://flagcdn.com/mh.svg'),
('mp', 'Northern Mariana Islands', 6, 'https://flagcdn.com/mp.svg'),
('nc', 'New Caledonia', 6, 'https://flagcdn.com/nc.svg'),
('nz', 'New Zealand', 6, 'https://flagcdn.com/nz.svg'),
('pf', 'French Polynesia', 6, 'https://flagcdn.com/pf.svg');


-- Châu Nam Cực
INSERT INTO countries (country_code, country_name, area_id, country_image) VALUES
('aq', 'Antarctica', 7, 'https://flagcdn.com/aq.svg');
-- Chèn dữ liệu cho bảng options
INSERT INTO options (duration, data, unit, is_daily_plan, description, price, currency) VALUES
(30, 10, 'GB', FALSE, '10GB data valid for 30 days', 29.99, 'USD'),
(15, 5, 'GB', FALSE, '5GB data valid for 15 days', 19.99, 'USD'),
(7, 2, 'GB', FALSE, '2GB data valid for 7 days', 9.99, 'USD'),
(1, 500, 'MB', TRUE, '500MB daily data', 4.99, 'USD'),
(30, 20, 'GB', FALSE, '20GB data valid for 30 days', 39.99, 'USD'),
(30, 50, 'GB', FALSE, '50GB data valid for 30 days', 59.99, 'USD');
-- Insert eSIMs for each country
INSERT INTO esims (esim_name, esim_tag, country_id, hotspot, policy, expiration, voice_calls_sms, verified, description, provider, esim_image)
SELECT 
    CONCAT(country_name, ' eSIM'),  -- eSIM name based on country name
    IF(RAND() > 0.5, TRUE, FALSE),  -- Randomly choose TRUE or FALSE for esim_tag
    country_id,
    TRUE,                          -- hotspot (enabled)
    'Standard Policy',             -- policy
    '30 days',                     -- expiration
    TRUE,                          -- voice_calls_sms (enabled)
    TRUE,                          -- verified (assumed to be true)
    CONCAT(country_name, ' eSIM for international roaming and data usage.'),  -- description
    'Global Provider',             -- provider
    country_image                  -- esim_image from country
FROM countries
WHERE area_id IN (1, 2, 3, 4, 5, 6, 7);  -- Insert eSIM for each country in all areas

-- Insert options for each eSIM
-- Option 1: Data plan
INSERT INTO options (duration, data, unit, is_daily_plan, description, price, currency)
VALUES 
(30, 100, 'GB', FALSE, '30GB for 30 days', 10.00, 'USD'),
(30, 500, 'MB', FALSE, '500MB for 30 days', 2.00, 'USD'); 

-- Option 2: Data plan
INSERT INTO options (duration, data, unit, is_daily_plan, description, price, currency)
VALUES 
(1, 100, 'MB', TRUE, '100MB per day for 1 day', 1.50, 'USD'),
(1, 50, 'MB', TRUE, '50MB per day for 1 day', 0.75, 'USD');

-- Link the eSIMs to options using esim_options
-- We will link each eSIM with at least two options (from the above example options)
-- Insert into esim_options with 30-day plans
INSERT INTO esim_options (esim_id, option_id, option_amount)
SELECT e.esim_id, o.option_id, 1000
FROM esims e
JOIN options o ON (o.duration = 30 AND o.is_daily_plan = FALSE)  -- Chọn các gói 30 ngày
JOIN countries c ON e.country_id = c.country_id  -- Kết hợp với bảng countries để so khớp country_id
WHERE e.country_id = c.country_id;


-- Chèn dữ liệu cho bảng coupons
INSERT INTO coupons (coupon_code, discount_value, discount_type, expiration_date, is_active, description) VALUES
('SUMMER2024', 10.00, 'PERCENTAGE', '2024-08-31', TRUE, 'Summer season discount'),
('WELCOME50', 50.00, 'AMOUNT', '2024-12-31', TRUE, 'New user welcome discount'),
('FLASH25', 25.00, 'PERCENTAGE', '2024-06-30', TRUE, 'Flash sale discount'),
('TRAVEL2024', 15.00, 'PERCENTAGE', '2024-12-31', TRUE, 'Travel season discount'),
('FIRSTTIME', 30.00, 'AMOUNT', '2024-12-31', TRUE, 'First purchase discount');


INSERT INTO orders (total_amount, email, full_name, phone_number,order_status) VALUES
(49.98,  'john.doe@email.com', 'John Doe', '+1234567890','PENDING'),
(79.97, 'mary.smith@email.com', 'Mary Smith', '+1987654321','COMPLETED'),
(29.99,  'peter.jones@email.com', 'Peter Jones', '+1122334455','COMPLETED'),
(159.96, 'sarah.wilson@email.com', 'Sarah Wilson', '+1555666777','COMPLETED'),
(89.97,  'mike.brown@email.com', 'Mike Brown', '+1999888777','PENDING');

-- Insert sample order items
-- For each order, we'll add 1-3 items with different eSIMs and options
-- Insert order items using valid esim_id and option_id combinations
INSERT INTO order_items (order_id, esim_id, option_id, quantity, price)
SELECT 
    1, eo.esim_id, eo.option_id, 1, 29.99
FROM esim_options eo
LIMIT 1;

INSERT INTO order_items (order_id, esim_id, option_id, quantity, price)
SELECT 
    2, eo.esim_id, eo.option_id, 2, 39.98
FROM esim_options eo
WHERE eo.esim_id != (SELECT esim_id FROM order_items WHERE order_id = 1)
LIMIT 1;

INSERT INTO order_items (order_id, esim_id, option_id, quantity, price)
SELECT 
    3, eo.esim_id, eo.option_id, 1, 29.99
FROM esim_options eo
WHERE eo.esim_id NOT IN (
    SELECT esim_id FROM order_items WHERE order_id IN (1,2)
)
LIMIT 1;

INSERT INTO order_items (order_id, esim_id, option_id, quantity, price)
SELECT 
    4, eo.esim_id, eo.option_id, 2, 59.98
FROM esim_options eo
WHERE eo.esim_id NOT IN (
    SELECT esim_id FROM order_items WHERE order_id IN (1,2,3)
)
LIMIT 1;

INSERT INTO order_items (order_id, esim_id, option_id, quantity, price)
SELECT 
    5, eo.esim_id, eo.option_id, 1, 29.99
FROM esim_options eo
WHERE eo.esim_id NOT IN (
    SELECT esim_id FROM order_items WHERE order_id IN (1,2,3,4)
)
LIMIT 1;
-- Insert sample payments
INSERT INTO payments (order_id, payment_amount, currency_code, payment_method, payment_status, 
                     transaction_id, paypal_payer_id, paypal_payer_email, payment_details) VALUES
-- Payment for Order 1
(1, 49.98, 'USD', 'PayPal', 'COMPLETED', 
'TXN123456789', 'PAYER123', 'john.doe@email.com',
JSON_OBJECT(
    'payment_source', 'PayPal',
    'payment_time', '2024-03-15 10:30:00',
    'currency', 'USD'
)),

-- Payment for Order 2
(2, 79.97, 'USD', 'PayPal', 'COMPLETED',
'TXN987654321', 'PAYER456', 'mary.smith@email.com',
JSON_OBJECT(
    'payment_source', 'PayPal',
    'payment_time', '2024-03-15 11:45:00',
    'currency', 'USD'
)),

-- Payment for Order 3
(3, 29.99, 'USD', 'PayPal', 'COMPLETED',
'TXN456789123', 'PAYER789', 'peter.jones@email.com',
JSON_OBJECT(
    'payment_source', 'PayPal',
    'payment_time', '2024-03-15 14:20:00',
    'currency', 'USD'
)),

-- Payment for Order 4
(4, 159.96, 'USD', 'PayPal', 'COMPLETED',
'TXN789123456', 'PAYER012', 'sarah.wilson@email.com',
JSON_OBJECT(
    'payment_source', 'PayPal',
    'payment_time', '2024-03-15 16:10:00',
    'currency', 'USD'
)),

-- Payment for Order 5
(5, 89.97, 'USD', 'PayPal', 'PENDING',
'TXN321654987', 'PAYER345', 'mike.brown@email.com',
JSON_OBJECT(
    'payment_source', 'PayPal',
    'payment_time', '2024-03-15 17:30:00',
    'currency', 'USD'
));


-- Chèn dữ liệu cho bảng FAQs
INSERT INTO faqs (question, answer) VALUES
('What is an eSIM?', 'An eSIM (embedded SIM) is a digital SIM that allows you to activate a cellular plan without having to use a physical SIM card.'),
('How do I activate my eSIM?', 'To activate your eSIM, scan the QR code we provide and follow the installation instructions on your device.'),
('Which devices are compatible?', 'Most recent smartphones support eSIM, including iPhone XS and newer, Google Pixel 3 and newer, and many Samsung devices.'),
('How long does activation take?', 'eSIM activation typically takes just a few minutes after scanning the QR code.'),
('Can I use multiple eSIMs?', 'Yes, most modern devices support multiple eSIM profiles, though only one can be active at a time.');

-- Chèn dữ liệu cho bảng About Us
INSERT INTO about_us (title, description, image) VALUES
('Global eSIM Provider', 'We are a leading provider of eSIM solutions, connecting travelers worldwide with reliable data plans.', 'https://images.unsplash.com/photo-1516321497487-e288fb19713f'),
('Our Mission', 'To make global connectivity simple, affordable, and accessible to everyone.', 'https://images.unsplash.com/photo-1553484771-047a44eee27b'),
('Customer First', 'We prioritize customer satisfaction with 24/7 support and reliable service.', 'https://images.unsplash.com/photo-1519389950473-47ba0277781c'),
('Global Network', 'Our extensive network of partners ensures you stay connected wherever you go.', 'https://images.unsplash.com/photo-1529704193007-e8c78f0f46f9'),
('Innovation Leaders', 'We are at the forefront of eSIM technology, constantly innovating to improve your experience.', 'https://images.unsplash.com/photo-1451187580459-43490279c0fa');

-- Insert Partner Hub
INSERT INTO partner_hub (title, description, image) VALUES
('Telecom Partners', 'We work with leading telecom providers worldwide to ensure the best coverage.', 'https://images.unsplash.com/photo-1560264280-88b68371db39'),
('Technology Partners', 'Partnering with top technology companies to deliver cutting-edge eSIM solutions.', 'https://images.unsplash.com/photo-1451187580459-43490279c0fa'),
('Travel Partners', 'Collaborating with travel agencies and services to enhance your journey.', 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05');
-- insert blogs
INSERT INTO blogs (sumary, blog_image, title, content, author)
VALUES 
    (
        "eSIM technology is revolutionizing mobile connectivity, offering a seamless experience without the need for physical SIM cards.",  -- Summary
        "https://d1hjkbq40fs2x4.cloudfront.net/2017-08-21/files/landscape-photography_1645-t.jpg",  -- Image URL
        "Understanding eSIM Technology: The Future of Connectivity",  -- Title
        "<h1>Understanding eSIM Technology: The Future of Connectivity</h1>
        <p>eSIM technology is revolutionizing how we connect to mobile networks. Unlike traditional SIM cards, an eSIM is embedded in the device, allowing users to switch carriers or plans without swapping physical cards. This change paves the way for a more seamless, flexible connectivity experience, especially for global travelers and IoT devices.</p>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. In-depth information about eSIM technology and its advantages over physical SIM cards. For instance, eSIMs enable remote provisioning, meaning users can activate a carrier profile via software rather than visiting a store. This allows users to add, delete, or switch between multiple network profiles on a single device, all remotely.</p>
        <p><strong>Key Benefits of eSIM:</strong></p>
        <ul>
            <li>Convenience and flexibility</li>
            <li>Better for the environment (no physical waste)</li>
            <li>Perfect for IoT applications</li>
            <li>Ideal for international travelers</li>
        </ul>
        <p>eSIMs also facilitate the seamless activation of dual SIM devices, which is increasingly popular among smartphone users. They’re also highly beneficial for IoT devices that need a secure and reliable connection in various locations. By supporting remote provisioning, eSIMs allow users to switch networks more easily.</p>
        <p>[Additional paragraphs with technical details, future of eSIMs, and potential industry impact]</p>",
        "ADMIN"  -- Author
    ),
    (
        "eSIMs offer more flexibility, security, and ease of use than traditional physical SIM cards, and they are rapidly gaining popularity.",  -- Summary
        "https://photo.znews.vn/w660/Uploaded/mdf_eioxrd/2021_07_06/2.jpg",  -- Image URL
        "eSIM vs. Physical SIM: What Are the Key Differences?",  -- Title
        "<h1>eSIM vs. Physical SIM: What Are the Key Differences?</h1>
        <p>The traditional physical SIM card is slowly being replaced by eSIM technology, and many users wonder about the key differences between the two. In this blog, we’ll explore how eSIMs differ from physical SIMs in terms of flexibility, security, and usability.</p>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Physical SIM cards have been a staple of mobile connectivity for decades, but they come with limitations, such as the need to insert or replace the card manually. In contrast, eSIMs are embedded directly into the device and can be activated or changed remotely.</p>
        <p><strong>Advantages of eSIM over Physical SIM:</strong></p>
        <ul>
            <li>Remote activation without needing a physical card</li>
            <li>Supports multiple profiles on one device</li>
            <li>Less prone to damage</li>
            <li>More secure due to built-in device authentication</li>
        </ul>
        <p>For users who frequently travel internationally, eSIMs offer the convenience of switching between carriers without needing to purchase a local SIM card in each country. Additionally, eSIMs are often more secure as they are embedded into the device, reducing the risk of tampering.</p>
        <p>[Additional sections discussing cost benefits, use cases, and security considerations]</p>",
        "ADMIN"  -- Author
    ),
    (
        "Switching to an eSIM is a simple process that offers more flexibility, especially for users who travel often or use multiple networks.",  -- Summary
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThbl47VAQK_3kDo3-L6d84Y2qX-f0TTUlgIQ&s",  -- Image URL
        "How to Switch to an eSIM: A Step-by-Step Guide",  -- Title
        "<h1>How to Switch to an eSIM: A Step-by-Step Guide</h1>
        <p>Switching from a physical SIM card to an eSIM can be a straightforward process, but it requires following a few specific steps. This guide will walk you through the necessary steps to transition to an eSIM and enjoy the benefits of a digital, more flexible connection.</p>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Before switching, you should ensure your device supports eSIM technology. Most modern smartphones, including the latest iPhone and Android models, are compatible with eSIMs. Contact your carrier to confirm if they offer eSIM activation.</p>
        <p><strong>Steps to Switch to an eSIM:</strong></p>
        <ol>
            <li>Check if your device supports eSIM</li>
            <li>Contact your carrier to activate eSIM service</li>
            <li>Download and activate the eSIM profile through QR code or app</li>
            <li>Test your eSIM connection</li>
        </ol>
        <p>Many carriers provide a QR code to activate the eSIM profile on your device. Simply scan the code with your phone’s camera, and follow the prompts to download the profile. Once downloaded, you’ll have the option to select this profile as your primary or secondary line, depending on your needs.</p>
        <p>[Additional tips for troubleshooting and managing multiple profiles]</p>",
        "ADMIN"  -- Author
    );
