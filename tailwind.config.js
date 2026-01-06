/** @type {import('tailwindcss').Config} */
export default {
	darkMode: ["class"],
	content: [
		"./index.html",
		"./src/**/*.{vue,js,ts,jsx,tsx}",
	],
	theme: {
		extend: {
			borderRadius: {
				lg: 'var(--radius)',
				md: 'calc(var(--radius) - 2px)',
				sm: 'calc(var(--radius) - 4px)'
			},
			colors: {
				background: 'hsl(var(--background))',
				foreground: 'hsl(var(--foreground))',
				card: {
					DEFAULT: 'hsl(var(--card))',
					foreground: 'hsl(var(--card-foreground))'
				},
				popover: {
					DEFAULT: 'hsl(var(--popover))',
					foreground: 'hsl(var(--popover-foreground))'
				},
				primary: {
					DEFAULT: 'hsl(var(--primary))',
					foreground: 'hsl(var(--primary-foreground))'
				},
				secondary: {
					DEFAULT: 'hsl(var(--secondary))',
					foreground: 'hsl(var(--secondary-foreground))'
				},
				muted: {
					DEFAULT: 'hsl(var(--muted))',
					foreground: 'hsl(var(--muted-foreground))'
				},
				accent: {
					DEFAULT: 'hsl(var(--accent))',
					foreground: 'hsl(var(--accent-foreground))'
				},
				destructive: {
					DEFAULT: 'hsl(var(--destructive))',
					foreground: 'hsl(var(--destructive-foreground))'
				},
				border: 'hsl(var(--border))',
				input: 'hsl(var(--input))',
				ring: 'hsl(var(--ring))',
				chart: {
					'1': 'hsl(var(--chart-1))',
					'2': 'hsl(var(--chart-2))',
					'3': 'hsl(var(--chart-3))',
					'4': 'hsl(var(--chart-4))',
					'5': 'hsl(var(--chart-5))'
				},
				legacy: {
					blue: {
						DEFAULT: 'var(--header-bg-end)',
						hover: '#006ecf',
						dark: 'var(--header-bg-start)' // Header main color
					},
					bg: {
						DEFAULT: 'var(--content-bg)',
						hover: '#F0F0F2',
						sidebar: 'var(--sidebar-bg)',
						sidebar_hover: 'var(--sidebar-item-hover)',
						header_gradient_start: 'var(--header-bg-start)',
						header_gradient_end: 'var(--header-bg-end)',
						row_hover: 'var(--row-hover)',
						toolbar: 'var(--toolbar-bg)',
						table_header_start: 'var(--table-header-start)',
						table_header_end: 'var(--table-header-end)'
					},
					border: {
						DEFAULT: 'var(--border-legacy)', // Benchmarked border
						sidebar: 'var(--border-sidebar)',
						light: '#E6E6E6'
					},
					text: {
						DEFAULT: 'var(--text-default)',
						body: 'var(--text-body)',
						muted: 'var(--text-muted)',
						header: 'var(--header-text)'
					},
					orange: '#ff8000'
				}
			},
			fontFamily: {
				dotum: ['"돋움"', 'Dotum', '"굴림"', 'Gulim', '"맑은 고딕"', 'Malgun Gothic', 'sans-serif'],
			}
		}
	},

	plugins: [require("tailwindcss-animate")],
}
