-- DEV ONLY — Do not run in production or staging environments
--
-- V999.2: Clean product data, users, addresses, and reviews
--
-- Execution order:
--   1. V1__initial_schema.sql
--   2. V2__add_missing_columns.sql
--   3. V999__dev_data.sql
--   4. V999.2__dev_data_v2.sql (this file)
--
-- TODO: BACKEND — Product rating values are pre-set snapshots independent
-- of seed review data. When the real API is ready, add a trigger or service
-- that recalculates products.rating dynamically from the reviews table
-- average whenever a review is inserted, updated, or deleted.

USE bazaar_db;

SET NAMES utf8mb4;

-- DELETE existing dirty products from V999
-- Cascades to cart_items and reviews referencing these product IDs.
-- Safe in dev environment only.

DELETE FROM bazaar_db.products
WHERE id IN (
    '22222222-2222-4222-8222-222222220001',
    '22222222-2222-4222-8222-222222220002',
    '22222222-2222-4222-8222-222222220003',
    '22222222-2222-4222-8222-222222220004',
    '22222222-2222-4222-8222-222222220005',
    '22222222-2222-4222-8222-222222220006',
    '22222222-2222-4222-8222-222222220007',
    '22222222-2222-4222-8222-222222220008',
    '22222222-2222-4222-8222-222222220009',
    '22222222-2222-4222-8222-222222220010',
    '22222222-2222-4222-8222-222222220011',
    '22222222-2222-4222-8222-222222220012',
    '22222222-2222-4222-8222-222222220013',
    '22222222-2222-4222-8222-222222220014',
    '22222222-2222-4222-8222-222222220015',
    '22222222-2222-4222-8222-222222220016',
    '22222222-2222-4222-8222-222222220017',
    '22222222-2222-4222-8222-222222220018',
    '22222222-2222-4222-8222-222222220019',
    '22222222-2222-4222-8222-222222220020',
    '22222222-2222-4222-8222-222222220021',
    '22222222-2222-4222-8222-222222220022',
    '22222222-2222-4222-8222-222222220023',
    '22222222-2222-4222-8222-222222220024',
    '22222222-2222-4222-8222-222222220025',
    '22222222-2222-4222-8222-222222220026',
    '22222222-2222-4222-8222-222222220027',
    '22222222-2222-4222-8222-222222220028',
    '22222222-2222-4222-8222-222222220029',
    '22222222-2222-4222-8222-222222220030',
    '22222222-2222-4222-8222-222222220031',
    '22222222-2222-4222-8222-222222220032',
    '22222222-2222-4222-8222-222222220033',
    '22222222-2222-4222-8222-222222220034',
    '22222222-2222-4222-8222-222222220035',
    '22222222-2222-4222-8222-222222220036',
    '22222222-2222-4222-8222-222222220037',
    '22222222-2222-4222-8222-222222220038',
    '22222222-2222-4222-8222-222222220039',
    '22222222-2222-4222-8222-222222220040',
    '22222222-2222-4222-8222-222222220041',
    '22222222-2222-4222-8222-222222220042',
    '22222222-2222-4222-8222-222222220043',
    '22222222-2222-4222-8222-222222220044',
    '22222222-2222-4222-8222-222222220045',
    '22222222-2222-4222-8222-222222220046',
    '22222222-2222-4222-8222-222222220047',
    '22222222-2222-4222-8222-222222220048',
    '22222222-2222-4222-8222-222222220049',
    '22222222-2222-4222-8222-222222220050',
    '22222222-2222-4222-8222-222222220051',
    '22222222-2222-4222-8222-222222220052',
    '22222222-2222-4222-8222-222222220053',
    '22222222-2222-4222-8222-222222220054'
);

-- INSERT clean products with all columns populated
-- Same UUIDs and category_ids as V999.
-- image_url uses picsum.photos seeds — swap for real CDN assets when ready.
-- TODO: BACKEND — Replace image_url values with real CDN asset URLs.

INSERT INTO bazaar_db.products (id, category_id, name, slug, description, price,
                                 stock_quantity, image_url, is_active, sku, rating,
                                 danger_level, sizes, created_at, updated_at)
VALUES
    ('22222222-2222-4222-8222-222222220001',
     'a1111111-1111-4111-8111-111111110012',
     'Portal Gun', 'portal-gun',
     'Handheld device for creating portals between dimensions.',
     299.99, 120, 'https://picsum.photos/seed/rm001/512/512', 1,
     'RM-001', 4.9, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220002',
     'a1111111-1111-4111-8111-111111110005',
     'Portal Fluid Refill', 'portal-fluid-refill',
     'Recharge fluid for portal gun.',
     49.99, 300, 'https://picsum.photos/seed/rm002/512/512', 1,
     'RM-002', 4.8, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220003',
     'a1111111-1111-4111-8111-111111110029',
     'Meeseeks Box', 'meeseeks-box',
     'Summons a Mr. Meeseeks to complete tasks. I''m Mr. Meeseeks, look at me!',
     199.99, 80, 'https://picsum.photos/seed/rm003/512/512', 1,
     'RM-003', 4.7, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220004',
     'a1111111-1111-4111-8111-111111110010',
     'Plumbus', 'plumbus',
     'Multi-purpose household device. Everyone has one.',
     89.99, 200, 'https://picsum.photos/seed/rm004/512/512', 1,
     'RM-004', 4.6, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220005',
     'a1111111-1111-4111-8111-111111110008',
     'Interdimensional Cable Box', 'interdimensional-cable-box',
     'Access to TV channels across all dimensions.',
     149.99, 95, 'https://picsum.photos/seed/rm005/512/512', 1,
     'RM-005', 4.8, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220006',
     'a1111111-1111-4111-8111-111111110018',
     'Microverse Battery', 'microverse-battery',
     'Mini universe energy source with virtually unlimited power.',
     129.99, 60, 'https://picsum.photos/seed/rm006/512/512', 1,
     'RM-006', 4.9, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220007',
     'a1111111-1111-4111-8111-111111110031',
     'Rick''s Laser Gun', 'ricks-laser-gun',
     'High-powered sci-fi blaster. Handle with care.',
     179.99, 140, 'https://picsum.photos/seed/rm007/512/512', 1,
     'RM-007', 4.7, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220008',
     'a1111111-1111-4111-8111-111111110031',
     'Neutrino Bomb', 'neutrino-bomb',
     'Planet-destroying device. Seriously, be careful.',
     399.99, 15, 'https://picsum.photos/seed/rm008/512/512', 1,
     'RM-008', 4.5, 'Extinction Class', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220009',
     'a1111111-1111-4111-8111-111111110028',
     'Teleportation Gun', 'teleportation-gun',
     'Instant teleportation tool for short and long range travel.',
     249.99, 100, 'https://picsum.photos/seed/rm009/512/512', 1,
     'RM-009', 4.8, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220010',
     'a1111111-1111-4111-8111-111111110003',
     'Memory Erasing Device', 'memory-erasing-device',
     'Removes specific memories with surgical precision.',
     159.99, 75, 'https://picsum.photos/seed/rm010/512/512', 1,
     'RM-010', 4.6, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220011',
     'a1111111-1111-4111-8111-111111110025',
     'Snake Time Travel Bracelet', 'snake-time-travel-bracelet',
     'Enables snake-style time travel through temporal loops.',
     129.99, 110, 'https://picsum.photos/seed/rm011/512/512', 1,
     'RM-011', 4.4, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220012',
     'a1111111-1111-4111-8111-111111110032',
     'Anti-Gravity Boots', 'anti-gravity-boots',
     'Walk on walls and ceilings with ease.',
     199.99, 130, 'https://picsum.photos/seed/rm012/512/512', 1,
     'RM-012', 4.7, 'Moderate',
     '["US 6","US 7","US 8","US 9","US 10","US 11"]',
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220013',
     'a1111111-1111-4111-8111-111111110002',
     'Clone Kit', 'clone-kit',
     'Create human clones with full genetic accuracy.',
     299.99, 50, 'https://picsum.photos/seed/rm013/512/512', 1,
     'RM-013', 4.8, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220014',
     'a1111111-1111-4111-8111-111111110005',
     'Tiny Rick Serum', 'tiny-rick-serum',
     'Temporarily turns user into a younger version of themselves.',
     59.99, 210, 'https://picsum.photos/seed/rm014/512/512', 1,
     'RM-014', 4.3, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220015',
     'a1111111-1111-4111-8111-111111110013',
     'Froopyland Portal Key', 'froopyland-portal-key',
     'Grants access to the Froopyland dimension.',
     89.99, 85, 'https://picsum.photos/seed/rm015/512/512', 1,
     'RM-015', 4.5, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220016',
     'a1111111-1111-4111-8111-111111110003',
     'Dream Inception Helmet', 'dream-inception-helmet',
     'Enter the dreams of others and navigate their subconscious.',
     179.99, 95, 'https://picsum.photos/seed/rm016/512/512', 1,
     'RM-016', 4.6, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220017',
     'a1111111-1111-4111-8111-111111110019',
     'Butter Robot', 'butter-robot',
     'Passes butter. What is my purpose?',
     79.99, 220, 'https://picsum.photos/seed/rm017/512/512', 1,
     'RM-017', 4.2, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220018',
     'a1111111-1111-4111-8111-111111110021',
     'Alien Parasite Detector', 'alien-parasite-detector',
     'Detects memory parasites hiding in plain sight.',
     139.99, 70, 'https://picsum.photos/seed/rm018/512/512', 1,
     'RM-018', 4.7, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220019',
     'a1111111-1111-4111-8111-111111110004',
     'Vindicators Beacon', 'vindicators-beacon',
     'Contact the Vindicators superhero team across dimensions.',
     99.99, 65, 'https://picsum.photos/seed/rm019/512/512', 1,
     'RM-019', 4.3, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220020',
     'a1111111-1111-4111-8111-111111110004',
     'Cromulon Translator', 'cromulon-translator',
     'Translate the Cromulon giant head language. Show me what you got!',
     119.99, 55, 'https://picsum.photos/seed/rm020/512/512', 1,
     'RM-020', 4.4, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220021',
     'a1111111-1111-4111-8111-111111110002',
     'Unity Neural Link', 'unity-neural-link',
     'Connect minds to a hive consciousness network.',
     249.99, 40, 'https://picsum.photos/seed/rm021/512/512', 1,
     'RM-021', 4.6, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220022',
     'a1111111-1111-4111-8111-111111110026',
     'Space Cruiser Repair Kit', 'space-cruiser-repair-kit',
     'Complete repair kit for spaceship components.',
     89.99, 150, 'https://picsum.photos/seed/rm022/512/512', 1,
     'RM-022', 4.5, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220023',
     'a1111111-1111-4111-8111-111111110015',
     'Gazorpazorpfield Plush', 'gazorpazorpfield-plush',
     'Alien cat plush toy based on the beloved Gazorpazorpfield character.',
     39.99, 300, 'https://picsum.photos/seed/rm023/512/512', 1,
     'RM-023', 4.8, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220024',
     'a1111111-1111-4111-8111-111111110008',
     'Schwifty Sound Generator', 'schwifty-sound-generator',
     'Produces music powerful enough to save entire planets.',
     69.99, 140, 'https://picsum.photos/seed/rm024/512/512', 1,
     'RM-024', 4.7, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220025',
     'a1111111-1111-4111-8111-111111110007',
     'Pickle Rick Kit', 'pickle-rick-kit',
     'Complete kit to turn yourself into a pickle. No questions asked.',
     59.99, 180, 'https://picsum.photos/seed/rm025/512/512', 1,
     'RM-025', 4.9, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220026',
     'a1111111-1111-4111-8111-111111110032',
     'Alien Goggles', 'alien-goggles',
     'See hidden alien forms invisible to the naked eye.',
     49.99, 210, 'https://picsum.photos/seed/rm026/512/512', 1,
     'RM-026', 4.4, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220027',
     'a1111111-1111-4111-8111-111111110025',
     'Time Freeze Remote', 'time-freeze-remote',
     'Pause time entirely at the press of a button.',
     199.99, 60, 'https://picsum.photos/seed/rm027/512/512', 1,
     'RM-027', 4.8, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220028',
     'a1111111-1111-4111-8111-111111110017',
     'Reality Stabilizer', 'reality-stabilizer',
     'Prevents dimension collapse and keeps reality stable.',
     179.99, 45, 'https://picsum.photos/seed/rm028/512/512', 1,
     'RM-028', 4.7, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220029',
     'a1111111-1111-4111-8111-111111110006',
     'Space Snake Egg', 'space-snake-egg',
     'Hatch your very own time-traveling space snake.',
     89.99, 100, 'https://picsum.photos/seed/rm029/512/512', 1,
     'RM-029', 4.3, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220030',
     'a1111111-1111-4111-8111-111111110009',
     'Blips and Chitz Game Cartridge', 'blips-and-chitz-game-cartridge',
     'Virtual life simulation game cartridge for Blips and Chitz.',
     59.99, 170, 'https://picsum.photos/seed/rm030/512/512', 1,
     'RM-030', 4.9, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220031',
     'a1111111-1111-4111-8111-111111110022',
     'Jerry Daycare Ticket', 'jerry-daycare-ticket',
     'Temporary Jerry storage solution. Drop-off and pick-up available.',
     29.99, 500, 'https://picsum.photos/seed/rm031/512/512', 1,
     'RM-031', 4.1, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220032',
     'a1111111-1111-4111-8111-111111110011',
     'Alien Language Chip', 'alien-language-chip',
     'Neural chip that grants understanding of all known languages.',
     79.99, 120, 'https://picsum.photos/seed/rm032/512/512', 1,
     'RM-032', 4.8, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220033',
     'a1111111-1111-4111-8111-111111110001',
     'Portal Gun Holster', 'portal-gun-holster',
     'Securely carry your portal gun on your hip at all times.',
     39.99, 200, 'https://picsum.photos/seed/rm033/512/512', 1,
     'RM-033', 4.6, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220034',
     'a1111111-1111-4111-8111-111111110016',
     'Dimensional Map', 'dimensional-map',
     'Comprehensive guide to navigating infinite universes.',
     99.99, 75, 'https://picsum.photos/seed/rm034/512/512', 1,
     'RM-034', 4.7, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220035',
     'a1111111-1111-4111-8111-111111110030',
     'Quantum Carburetor', 'quantum-carburetor',
     'High-performance carburetor for spaceship engines.',
     149.99, 90, 'https://picsum.photos/seed/rm035/512/512', 1,
     'RM-035', 4.5, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220036',
     'a1111111-1111-4111-8111-111111110002',
     'Clone Remote', 'clone-remote',
     'Remote control device for managing clone actions.',
     129.99, 55, 'https://picsum.photos/seed/rm036/512/512', 1,
     'RM-036', 4.6, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220037',
     'a1111111-1111-4111-8111-111111110027',
     'Interdimensional Passport', 'interdimensional-passport',
     'Official legal access document for interdimensional travel.',
     59.99, 220, 'https://picsum.photos/seed/rm037/512/512', 1,
     'RM-037', 4.4, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220038',
     'a1111111-1111-4111-8111-111111110024',
     'Hologram Projector', 'hologram-projector',
     'Create highly realistic holograms for any purpose.',
     109.99, 130, 'https://picsum.photos/seed/rm038/512/512', 1,
     'RM-038', 4.7, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220039',
     'a1111111-1111-4111-8111-111111110032',
     'Space Suit', 'space-suit',
     'Full-body suit for survival in hostile alien environments.',
     199.99, 85, 'https://picsum.photos/seed/rm039/512/512', 1,
     'RM-039', 4.8, 'Safe',
     '["XS","S","M","L","XL","XXL"]',
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220040',
     'a1111111-1111-4111-8111-111111110020',
     'Alien DNA Scanner', 'alien-dna-scanner',
     'Advanced scanner for analyzing alien genetic material.',
     139.99, 70, 'https://picsum.photos/seed/rm040/512/512', 1,
     'RM-040', 4.6, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220041',
     'a1111111-1111-4111-8111-111111110016',
     'Multiverse GPS', 'multiverse-gps',
     'Precision GPS for locating any dimension in the multiverse.',
     149.99, 95, 'https://picsum.photos/seed/rm041/512/512', 1,
     'RM-041', 4.9, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220042',
     'a1111111-1111-4111-8111-111111110003',
     'Brain Enhancer Helmet', 'brain-enhancer-helmet',
     'Temporarily boosts intelligence to genius-level capacity.',
     179.99, 60, 'https://picsum.photos/seed/rm042/512/512', 1,
     'RM-042', 4.5, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220043',
     'a1111111-1111-4111-8111-111111110032',
     'Portal Detector Watch', 'portal-detector-watch',
     'Wrist-worn device that detects nearby active portals.',
     89.99, 140, 'https://picsum.photos/seed/rm043/512/512', 1,
     'RM-043', 4.6, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220044',
     'a1111111-1111-4111-8111-111111110001',
     'Alien Pet Carrier', 'alien-pet-carrier',
     'Secure and comfortable carrier for transporting alien creatures.',
     69.99, 180, 'https://picsum.photos/seed/rm044/512/512', 1,
     'RM-044', 4.3, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220045',
     'a1111111-1111-4111-8111-111111110018',
     'Space Fuel Cell', 'space-fuel-cell',
     'High-efficiency fuel cell for powering spacecraft.',
     59.99, 250, 'https://picsum.photos/seed/rm045/512/512', 1,
     'RM-045', 4.7, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220046',
     'a1111111-1111-4111-8111-111111110028',
     'Teleport Beacon', 'teleport-beacon',
     'Plant a beacon to mark precise teleportation destinations.',
     119.99, 110, 'https://picsum.photos/seed/rm046/512/512', 1,
     'RM-046', 4.8, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220047',
     'a1111111-1111-4111-8111-111111110031',
     'Gravity Gun', 'gravity-gun',
     'Manipulate gravity fields in any direction.',
     189.99, 65, 'https://picsum.photos/seed/rm047/512/512', 1,
     'RM-047', 4.7, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220048',
     'a1111111-1111-4111-8111-111111110032',
     'Reality Goggles', 'reality-goggles',
     'See alternate realities overlaid on the current dimension.',
     99.99, 125, 'https://picsum.photos/seed/rm048/512/512', 1,
     'RM-048', 4.6, 'Moderate', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220049',
     'a1111111-1111-4111-8111-111111110004',
     'Alien Translator Earbud', 'alien-translator-earbud',
     'Earbud device for instant translation of any alien language.',
     79.99, 200, 'https://picsum.photos/seed/rm049/512/512', 1,
     'RM-049', 4.9, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220050',
     'a1111111-1111-4111-8111-111111110025',
     'Time Loop Device', 'time-loop-device',
     'Repeat time cycles endlessly until you get it right.',
     199.99, 45, 'https://picsum.photos/seed/rm050/512/512', 1,
     'RM-050', 4.8, 'Dangerous', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220051',
     'a1111111-1111-4111-8111-111111110023',
     'Interdimensional Backpack', 'interdimensional-backpack',
     'Backpack with infinite internal storage capacity.',
     89.99, 150, 'https://picsum.photos/seed/rm051/512/512', 1,
     'RM-051', 4.7, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220052',
     'a1111111-1111-4111-8111-111111110014',
     'Alien Food Replicator', 'alien-food-replicator',
     'Instantly replicate any food from across the universe.',
     129.99, 95, 'https://picsum.photos/seed/rm052/512/512', 1,
     'RM-052', 4.6, 'Safe', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220053',
     'a1111111-1111-4111-8111-111111110011',
     'Mind Control Chip', 'mind-control-chip',
     'Neural chip for controlling other beings. Use responsibly.',
     149.99, 50, 'https://picsum.photos/seed/rm053/512/512', 1,
     'RM-053', 4.4, 'Extinction Class', NULL,
     NOW(), NOW()),

    ('22222222-2222-4222-8222-222222220054',
     'a1111111-1111-4111-8111-111111110032',
     'Dimension Stabilizing Boots', 'dimension-stabilizing-boots',
     'Boots that prevent accidental slipping between realities.',
     179.99, 70, 'https://picsum.photos/seed/rm054/512/512', 1,
     'RM-054', 4.8, 'Moderate',
     '["US 6","US 7","US 8","US 9","US 10","US 11"]',
     NOW(), NOW());

-- INSERT users
-- Password for all users: password123
-- TODO: BACKEND — Replace placeholder bcrypt hash with one generated by
-- the application auth service before deploying to any non-dev environment.

INSERT INTO bazaar_db.users (id, first_name, last_name, email, password_hash,
                              role, phone, created_at)
VALUES
    ('33333333-3333-4333-8333-333333330001',
     'Rick', 'Sanchez', 'rick@citadel.test',
     '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'admin', '416-555-0001', NOW()),

    ('33333333-3333-4333-8333-333333330002',
     'Morty', 'Smith', 'morty@smith.test',
     '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'customer', '416-555-0002', NOW()),

    ('33333333-3333-4333-8333-333333330003',
     'Beth', 'Smith', 'beth@smith.test',
     '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'customer', '416-555-0003', NOW()),

    ('33333333-3333-4333-8333-333333330004',
     'Jerry', 'Smith', 'jerry@smith.test',
     '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'customer', '416-555-0004', NOW()),

    ('33333333-3333-4333-8333-333333330005',
     'Summer', 'Smith', 'summer@smith.test',
     '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'customer', '416-555-0005', NOW());

-- INSERT addresses
-- Rick, Morty, Beth, Summer: same address for delivery and billing (1 row)
-- Jerry: different delivery and billing addresses (2 rows)

INSERT INTO bazaar_db.addresses (id, user_id, full_name, street, city,
                                  province_state, country, postal_code,
                                  is_default_delivery, is_default_billing,
                                  created_at)
VALUES
    -- Rick — same address for both
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbb0001',
     '33333333-3333-4333-8333-333333330001',
     'Rick Sanchez', '123 Dimension C-137',
     'Citadel of Ricks', 'ON', 'CA', 'M5V 3A8',
     TRUE, TRUE, NOW()),

    -- Morty — same address for both
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbb0002',
     '33333333-3333-4333-8333-333333330002',
     'Morty Smith', '456 Smith Residence',
     'Toronto', 'ON', 'CA', 'M4B 1B3',
     TRUE, TRUE, NOW()),

    -- Beth — same address for both
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbb0003',
     '33333333-3333-4333-8333-333333330003',
     'Beth Smith', '456 Smith Residence',
     'Toronto', 'ON', 'CA', 'M4B 1B3',
     TRUE, TRUE, NOW()),

    -- Jerry — default delivery address
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbb0004',
     '33333333-3333-4333-8333-333333330004',
     'Jerry Smith', '456 Smith Residence',
     'Toronto', 'ON', 'CA', 'M4B 1B3',
     TRUE, FALSE, NOW()),

    -- Jerry — default billing address (different)
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbb0005',
     '33333333-3333-4333-8333-333333330004',
     'Jerry Smith', '789 Billing Avenue',
     'Mississauga', 'ON', 'CA', 'L5B 2T4',
     FALSE, TRUE, NOW()),

    -- Summer — same address for both
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbb0006',
     '33333333-3333-4333-8333-333333330005',
     'Summer Smith', '456 Smith Residence',
     'Toronto', 'ON', 'CA', 'M4B 1B3',
     TRUE, TRUE, NOW());

-- INSERT reviews
-- TODO: BACKEND — Product rating values are pre-set snapshots independent
-- of these review rows. When the real API is ready, add a trigger or service
-- that recalculates products.rating dynamically from the reviews table
-- average whenever a review is inserted, updated, or deleted.

INSERT INTO bazaar_db.reviews (id, product_id, user_id, rating, title, body, created_at)
VALUES
    -- Portal Gun reviews
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa001',
     '22222222-2222-4222-8222-222222220001',
     '33333333-3333-4333-8333-333333330002',
     5, 'Best thing ever!',
     'My life is divided into before and after using this. HIGHLY RECOMMEND.',
     '2026-04-01 10:00:00'),

    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa002',
     '22222222-2222-4222-8222-222222220001',
     '33333333-3333-4333-8333-333333330003',
     3, 'More colours???',
     'Would be nice if it came in other colours. Functions well though.',
     '2026-04-02 11:00:00'),

    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa003',
     '22222222-2222-4222-8222-222222220001',
     '33333333-3333-4333-8333-333333330004',
     2, 'Alright!',
     'Decent.. but just decent.',
     '2026-04-03 09:00:00'),

    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa004',
     '22222222-2222-4222-8222-222222220001',
     '33333333-3333-4333-8333-333333330005',
     4, 'Would buy again!',
     'It worked as expected, easy to get a grasp.',
     '2026-04-04 14:00:00'),

    -- Anti-Gravity Boots reviews
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa005',
     '22222222-2222-4222-8222-222222220012',
     '33333333-3333-4333-8333-333333330002',
     5, 'Worth every penny',
     'Walking on ceilings has never been easier. My cat is terrified.',
     '2026-04-03 12:00:00'),

    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa006',
     '22222222-2222-4222-8222-222222220012',
     '33333333-3333-4333-8333-333333330003',
     4, 'Great but sizing runs small',
     'Love the product, had to size up. Otherwise perfect.',
     '2026-04-05 15:00:00'),

    -- Meeseeks Box reviews
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa007',
     '22222222-2222-4222-8222-222222220003',
     '33333333-3333-4333-8333-333333330004',
     4, 'Very helpful!',
     'Asked it to fix my golf swing. Still working on it.',
     '2026-04-06 10:00:00'),

    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa008',
     '22222222-2222-4222-8222-222222220003',
     '33333333-3333-4333-8333-333333330005',
     3, 'Handle with care',
     'Works great for simple tasks. Do NOT ask it to solve existential problems.',
     '2026-04-07 16:00:00'),

    -- Pickle Rick Kit reviews
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa009',
     '22222222-2222-4222-8222-222222220025',
     '33333333-3333-4333-8333-333333330002',
     5, 'I am Pickle Rick!',
     'Exactly as advertised. 10/10 would pickle again.',
     '2026-04-08 11:00:00'),

    -- Neutrino Bomb reviews
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaa010',
     '22222222-2222-4222-8222-222222220008',
     '33333333-3333-4333-8333-333333330003',
     2, 'Too powerful honestly',
     'Does exactly what it says. Maybe too well. Use with extreme caution.',
     '2026-04-09 13:00:00');
