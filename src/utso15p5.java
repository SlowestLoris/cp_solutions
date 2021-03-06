import java.util.*;
import java.io.*;
public class utso15p5 {
	
	static class edge implements Comparable<edge>{
		int u, v, w;
		edge(int u1, int v1, int w1) {
			u = u1; v = v1; w = w1;
		}
		public int compareTo(edge z) {
			return Integer.compare(w, z.w);
		}
	}
	
	static class p {
		int f, s;
		p(int f1, int s1) {
			f = f1; s = s1;
		}
	}
	
	static final int MM = 50002, K = 16, INF = Integer.MAX_VALUE;
	static int N, M, p[] = new int[MM], lca[][] = new int[K][MM], val[][] = new int[K][MM], dep[] = new int[MM], mst, ans = INF, cnt = 0;
	static boolean vis[] = new boolean[100003];
	static edge[] e = new edge[100003];
	static ArrayList<p> adj[] = new ArrayList[MM];
	
	static int fd(int d) {
		if(d==p[d]) return p[d];
		return p[d] = fd(p[d]);
	}
	
	static void kruskal() {
		Arrays.sort(e);
		for(int i=0;i<M;i++) {
			int fu = fd(e[i].u), fv = fd(e[i].v);
			if(fu != fv) {
				p[fu] = fv; cnt++; vis[i]=true;mst+=e[i].w;
				adj[e[i].u].add(new p(e[i].v,e[i].w));
			}
			if(cnt == N-1) return;
		}
	}
	
	static void dfs(int u, int pa) {
		for(p t: adj[u]) {
			if(t.f == pa) continue;
			dep[t.f] = dep[u]+1; lca[0][t.f]=u; val[0][t.f]=t.s; dfs(t.f, u);
		}
	}
	
	static void build() {
		for(int i=1;i<K;i++) {
			for(int j=1;j<=N;j++) {
				if(lca[i][j] != -1) {
					lca[i][j] = lca[i-1][lca[i-1][j]]; val[i][j] = Math.max(val[i-1][j], val[i-1][lca[i-1][j]]);
				}
			}
		}
	}
	
	static int getLCA(int u, int v) {
		if(dep[u] < dep[v]) {
			int t = dep[u];
			dep[u] = dep[v];
			dep[v] = t;
		}
		for(int i=K-1;i>=0;i--) {
			if(lca[i][u] != -1 && dep[lca[i][u]] >= dep[v]) u = lca[i][u];
		}
		if(u==v) return v;
		for(int i=K-1;i>=0;i--) {
			if(lca[i][u]!=-1 && lca[i][v] != -1 && lca[i][u] != lca[i][v]) {
				u = lca[i][u]; v = lca[i][v];
			}
		}
		return lca[0][u];
	}
	
	static int query(int u, int rt) {
		int ret = 0;
		for(int i=K-1;i>=0;i--) {
			if(dep[u] >= dep[rt] + (1<<i)) {
				ret = Math.max(ret, val[i][u]); u = lca[i][u];
			}
		}
		return ret;
	}
	
	static void solve() {
		if(cnt != N-1) {
			pr.println(-1);
			return;
		}
		for(int i=0;i<K;i++) {
			for(int j=0;j<MM;j++) {
				lca[i][j] = -1;
				val[i][j] = -1;
			}
		}		
		dfs(1,0); build();
		for(int i=0;i<M;i++) {
			if(vis[i]) continue;
			int rt = getLCA(e[i].u, e[i].v), maxCost = Math.max(query(e[i].u, rt), rt);
			int sec = mst + e[i].w - maxCost;
			if(sec != mst && sec < ans) ans = sec;
		}
		pr.println(ans==INF?-1:ans);
	}

	public static void main(String[] args) throws IOException {
		int N = readInt(), M = readInt();
		for(int i=0;i<100003;i++) e[i] = new edge(0,0,Integer.MAX_VALUE);
		for(int i=0;i<M;i++) {
			e[i] = new edge(readInt(), readInt(), readInt());
		}
		for(int i=1;i<=N;i++) p[i] = i;
		kruskal(); solve();
		pr.close();
	}

	final private static int BUFFER_SIZE = 1 << 16;
	private static DataInputStream din = new DataInputStream(System.in);
	private static byte[] buffer = new byte[BUFFER_SIZE];
	private static int bufferPointer = 0, bytesRead = 0;
	static PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

	public static String readLine() throws IOException {
		byte[] buf = new byte[64]; // line length
		int cnt = 0, c;
		while ((c = Read()) != -1) {
			if (c == '\n')
				break;
			buf[cnt++] = (byte) c;
		}
		return new String(buf, 0, cnt);
	}
	public static String read() throws IOException{
		byte[] ret = new byte[1024];
        int idx = 0;
        byte c = Read();
        while (c <= ' ') {
            c = Read();
        }
        do {
            ret[idx++] = c;
            c = Read();
        } while (c != -1 && c != ' ' && c != '\n' && c != '\r');
        return new String(ret, 0, idx);
	}
	public static int readInt() throws IOException {
		int ret = 0;
		byte c = Read();
		while (c <= ' ')
			c = Read();
		boolean neg = (c == '-');
		if (neg)
			c = Read();
		do {
			ret = ret * 10 + c - '0';
		} while ((c = Read()) >= '0' && c <= '9');

		if (neg)
			return -ret;
		return ret;
	}

	public static long readLong() throws IOException {
		long ret = 0;
		byte c = Read();
		while (c <= ' ')
			c = Read();
		boolean neg = (c == '-');
		if (neg)
			c = Read();
		do {
			ret = ret * 10 + c - '0';
		} while ((c = Read()) >= '0' && c <= '9');
		if (neg)
			return -ret;
		return ret;
	}

	public static double readDouble() throws IOException {
		double ret = 0, div = 1;
		byte c = Read();
		while (c <= ' ')
			c = Read();
		boolean neg = (c == '-');
		if (neg)
			c = Read();

		do {
			ret = ret * 10 + c - '0';
		} while ((c = Read()) >= '0' && c <= '9');

		if (c == '.') {
			while ((c = Read()) >= '0' && c <= '9') {
				ret += (c - '0') / (div *= 10);
			}
		}

		if (neg)
			return -ret;
		return ret;
	}

	private static void fillBuffer() throws IOException {
		bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
		if (bytesRead == -1)
			buffer[0] = -1;
	}

	private static byte Read() throws IOException {
		if (bufferPointer == bytesRead)
			fillBuffer();
		return buffer[bufferPointer++];
	}

	public void close() throws IOException {
		if (din == null)
			return;
		din.close();
	}

	static void print(Object o) {
		pr.print(o);
	}

	static void println(Object o) {
		pr.println(o);
	}

	static void flush() {
		pr.flush();
	}

	static void println() {
		pr.println();
	}

	static void exit() throws IOException {
		din.close();
		pr.close();
		System.exit(0);
	}
}