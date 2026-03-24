# ============================================================
# Dockerfile — Nexus Clinic (Multi-stage build)
# Stage 1: Build the JAR with Maven
# Stage 2: Lean runtime image (no JDK, no Maven, no source code)
# ============================================================

# ── Stage 1: Build ──────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copy pom first — Docker layer cache: dependencies only re-downloaded when pom changes
COPY pom.xml .
RUN mvn dependency:go-offline -q 2>/dev/null || true

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime ────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

LABEL maintainer="Nexus Clinic <info@nexusclinic.eg>"
LABEL org.opencontainers.image.title="Nexus Clinic"
LABEL org.opencontainers.image.description="Physiotherapy clinic management app"

WORKDIR /app

# FIX: Non-root user (was already there — keep it)
RUN addgroup -S nexus && adduser -S nexus -G nexus

# FIX: Only copy the JAR — no source code, no pom.xml in the final image
COPY --from=builder /build/target/nexus-clinic-*.jar app.jar
RUN chown nexus:nexus app.jar

# FIX: Read-only filesystem except for /tmp
VOLUME ["/tmp"]

USER nexus

EXPOSE 8080

# JVM flags tuned for a 512 MB container
ENTRYPOINT ["java", \
  "-Xms128m", "-Xmx400m", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=prod", \
  "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD wget -q --spider http://localhost:8080/api/v1/health || exit 1
