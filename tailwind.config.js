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
						DEFAULT: '#005fb0',
						hover: '#006ecf',
						dark: '#2c609c' // Header main color
					},
					bg: {
						DEFAULT: '#F7F7F7',
						hover: '#F0F0F2',
						sidebar: '#EEFDFF',
						header_gradient_start: '#F0F4F7',
						header_gradient_end: '#D9E1E8',
						row_hover: '#FFF9C4'
					},
					border: {
						DEFAULT: '#DDDDDD', // Benchmarked border
						light: '#E6E6E6'
					},
					text: {
						DEFAULT: '#333333',
						body: '#555555',
						muted: '#696969'
					},
					orange: '#ff8000'
				}
			},
			fontFamily: {
				dotum: ['"맑은 고딕"', 'Malgun Gothic', '"돋움"', 'Dotum', '"굴림"', 'Gulim', 'sans-serif'],
			}
		}
	},

	plugins: [require("tailwindcss-animate")],
}
